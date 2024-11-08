DROP TABLE IF EXISTS test;
CREATE TABLE test
(
    id serial PRIMARY KEY,
    name VARCHAR,
    description VARCHAR
);

select * from test;
drop table test;

create table country(
    id serial primary key,
    name varchar(100)
);

create table city (
    id serial primary key,
    name varchar(100),
    post_number varchar(10),
    country_id int,
    foreign key (country_id) references country(id)
);

create table address (
    id serial primary key,
    street_name varchar(100),
    number varchar(5),
    city_id int,
    foreign key (city_id) references city(id)
);

create table registration (
    id serial primary key,
    username varchar(100),
    password varchar(100),
    is_admin bool,
    creation_date date DEFAULT CURRENT_DATE
);

create table customer_information (
    id serial primary key,
    registration_id int,
    first_name varchar(100),
    last_name varchar(100),
    phone varchar(20),
    delivery_address_id int,
    foreign key (registration_id) references registration(id),
    foreign key (delivery_address_id) references address(id)
);

create table subscription (
    id serial primary key,
    registration_id int,
    creation_date date DEFAULT CURRENT_DATE,
    cancel_date date null,
    foreign key (registration_id) references registration(id)
);

create table admin_week (
    id serial primary key,
    created_by_id int,
    week_start_date date,
    week_end_date date,
    creation_date date DEFAULT CURRENT_DATE,
    modification_date date null,
    foreign key (created_by_id) references registration(id)
);

create table selection (
    id serial primary key,
    created_by_id int,
    admin_week_id int,
    foreign key (created_by_id) references subscription(id),
    foreign key (admin_week_id) references admin_week(id)
);

create table recipe (
    id serial primary key,
    name varchar(100),
    type VARCHAR(15) DEFAULT 'vegan',
    contains_allergen bool default false,
    calories int default 0,
    creation_date date DEFAULT CURRENT_DATE,
    modification_date date null,
    image_link varchar(100)
);

create table ingredient (
    id serial primary key,
    name varchar(100),
    calories int,
    is_allergen bool,
    creation_date date DEFAULT CURRENT_DATE,
    modification_date date null
);

create table type (
    id serial primary key,
    type varchar(15) CHECK (type IN ('vegan', 'vegetarian', 'pescatarian', 'unrestricted'))
);

create table ingredient_type (
    id serial primary key,
    type_id int,
    ingredient_id int,
    foreign key (type_id) references type(id),
    foreign key (ingredient_id) references ingredient(id)
);

create table recipes_with_ingredients (
    id serial primary key,
    recipe_id int,
    ingredient_id int,
    foreign key (recipe_id) references recipe(id),
    foreign key (ingredient_id) references ingredient(id)
);

create table admin_week_recipes (
    id serial primary key,
    recipe_id int,
    admin_week_id int,
    foreign key (recipe_id) references recipe(id),
    foreign key (admin_week_id) references admin_week(id)
);

create table selection_recipe (
    id serial primary key,
    recipe_id int,
    selection_id int,
    foreign key (recipe_id) references recipe(id),
    foreign key (selection_id) references selection(id)
);

CREATE OR REPLACE FUNCTION update_recipe_on_ingredient_add()
RETURNS TRIGGER AS $$
DECLARE
    ingredient_calories INT;
    ingredient_is_allergen BOOLEAN;
    current_recipe_type VARCHAR(15);
    ingredient_type VARCHAR(15);
    highest_type VARCHAR(15) := 'unrestricted';  -- Default to the least restrictive type
BEGIN
    -- Fetch the ingredient's details
    SELECT calories, is_allergen INTO ingredient_calories, ingredient_is_allergen
    FROM ingredient
    WHERE id = NEW.ingredient_id;

    -- Update the calories in the recipe
    UPDATE recipe
    SET calories = calories + ingredient_calories
    WHERE id = NEW.recipe_id;

    -- Update the allergen status in the recipe
    IF ingredient_is_allergen THEN
        UPDATE recipe
        SET contains_allergen = TRUE
        WHERE id = NEW.recipe_id;
    END IF;

    -- Fetch the current recipe type
    SELECT type INTO current_recipe_type FROM recipe WHERE id = NEW.recipe_id;

    -- Loop through all types of the current ingredient and determine the highest type
    FOR ingredient_type IN
        SELECT t.type
        FROM ingredient_type it
        JOIN type t ON it.type_id = t.id
        WHERE it.ingredient_id = NEW.ingredient_id
    LOOP
        -- Adjust the highest type based on hierarchy
        IF highest_type = 'vegan' THEN
            -- Keep vegan as highest; no need to change
            CONTINUE;
        ELSIF highest_type = 'vegetarian' AND (ingredient_type = 'vegan') THEN
            highest_type := 'vegan';
        ELSIF highest_type = 'pescatarian' AND (ingredient_type IN ('vegan', 'vegetarian')) THEN
            highest_type := ingredient_type;
        ELSIF highest_type = 'unrestricted' THEN
            -- Update to more restrictive type if available
            highest_type := ingredient_type;
        END IF;
    END LOOP;

    -- Update recipe type if the new highest type is more restrictive
    IF (current_recipe_type = 'vegan' AND highest_type <> 'vegan') OR
       (current_recipe_type = 'vegetarian' AND highest_type NOT IN ('vegan', 'vegetarian')) OR
       (current_recipe_type = 'pescatarian' AND highest_type = 'unrestricted') THEN
        UPDATE recipe SET type = highest_type WHERE id = NEW.recipe_id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trigger_update_recipe
AFTER INSERT ON recipes_with_ingredients
FOR EACH ROW
EXECUTE FUNCTION update_recipe_on_ingredient_add();

CREATE OR REPLACE FUNCTION check_recipe_in_admin_week()
RETURNS TRIGGER AS $$
DECLARE
    admin_week_id_var INT;
BEGIN
    -- Retrieve the admin_week_id for the selection in question
    SELECT admin_week_id INTO admin_week_id_var
    FROM selection
    WHERE id = NEW.selection_id;

    -- Check if the recipe exists in the admin_week_recipes for this admin_week_id
    IF NOT EXISTS (
        SELECT 1
        FROM admin_week_recipes
        WHERE recipe_id = NEW.recipe_id
        AND admin_week_recipes.admin_week_id = admin_week_id_var
    ) THEN
        RAISE EXCEPTION 'Recipe % is not available in the admin week % for this customer week.',
                        NEW.recipe_id, admin_week_id_var;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create the trigger for insertions on the selection_recipe table
CREATE TRIGGER trg_check_recipe_in_admin_week
BEFORE INSERT ON selection_recipe
FOR EACH ROW
EXECUTE FUNCTION check_recipe_in_admin_week();

CREATE OR REPLACE FUNCTION check_cancel_date_before_insert()
RETURNS TRIGGER AS $$
BEGIN
    -- Check if the cancel_date is NULL in the subscription table
    IF (SELECT cancel_date FROM subscription WHERE id = NEW.created_by_id) IS NOT NULL THEN
        RAISE EXCEPTION 'Cannot insert into selection: Cancel date is not NULL.';
    END IF;

    -- If the check passes, allow the insert
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_cancel_date
BEFORE INSERT ON selection
FOR EACH ROW
EXECUTE FUNCTION check_cancel_date_before_insert();

CREATE OR REPLACE FUNCTION update_cancel_date()
RETURNS TRIGGER AS $$
BEGIN
    -- Check if the cancel_date is less than the registration_date
    IF NEW.cancel_date < (SELECT creation_date FROM subscription WHERE id = NEW.id) OR NEW.cancel_date IS NULL THEN
        -- Update cancel_date to NULL
        NEW.cancel_date := NULL;
    END IF;

    -- Allow the update
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_cancel_date
AFTER UPDATE ON subscription
FOR EACH ROW
EXECUTE FUNCTION update_cancel_date();


INSERT INTO country (name) VALUES ('Denmark'), ('Germany');

-- Insert mock data for `city`
INSERT INTO city (name, post_number, country_id) VALUES
('Aarhus', '8000', 1),
('Copenhagen', '1000', 1),
('Berlin', '10115', 2);

-- Insert mock data for `address`
INSERT INTO address (street_name, number, city_id) VALUES
('Main St', '12A', 1),
('Second St', '34B', 2),
('Third St', '56C', 3);

-- Insert mock data for `registration`
INSERT INTO registration (username, password, is_admin) VALUES
('user1', 'pass1', false),
('admin1', 'pass2', true);

-- Insert mock data for `customer_information`
INSERT INTO customer_information (registration_id, first_name, last_name, phone, delivery_address_id) VALUES
(1, 'John', 'Doe', '1234567890', 1),
(2, 'Jane', 'Smith', '0987654321', 2);

-- Insert mock data for `subscription`
INSERT INTO subscription (registration_id) VALUES
(1),
(2);

-- Insert mock data for `admin_week`
INSERT INTO admin_week (created_by_id, week_start_date, week_end_date) VALUES
(2, '2023-10-01', '2023-10-07');

-- Insert mock data for `selection`
INSERT INTO selection (created_by_id, admin_week_id) VALUES
(1, 1),
(2, 1);

-- Insert mock data for `recipe`
INSERT INTO recipe (name) VALUES
('Spaghetti Bolognese'),
('Vegetable Stir Fry'),
('Salmon Salad'),
('Chicken Curry');  -- Added Chicken Curry recipe

-- Insert mock data for `ingredient`
INSERT INTO ingredient (name, calories, is_allergen) VALUES
('Pasta', 200, false),
('Tomato Sauce', 50, false),
('Salmon', 150, false),
('Tofu', 100, false),
('Peanuts', 300, true),    -- Allergen: Peanuts
('Cream', 100, true),      -- Allergen: Cream
('Chicken', 250, false);   -- Non-allergenic Chicken

-- Insert mock data for `type`
INSERT INTO type (type) VALUES
('vegan'),
('vegetarian'),
('pescatarian'),
('unrestricted');

-- Insert mock data for `ingredient_type`
INSERT INTO ingredient_type (type_id, ingredient_id) VALUES
(1, 2),    -- Tomato Sauce is vegan
(2, 1),    -- Pasta is vegetarian
(3, 3),    -- Salmon is pescatarian
(1, 4),    -- Tofu is vegan
(2, 5),    -- Peanuts are vegetarian
(4, 6);    -- Cream is unrestricted

-- Bind ingredients to recipes
INSERT INTO recipes_with_ingredients (recipe_id, ingredient_id) VALUES
(1, 1),  -- Spaghetti Bolognese includes Pasta
(1, 2),  -- Spaghetti Bolognese includes Tomato Sauce
(2, 4),  -- Vegetable Stir Fry includes Tofu
(3, 3),  -- Salmon Salad includes Salmon
(3, 5),  -- Salmon Salad also includes Peanuts (allergen)
(4, 6),  -- Chicken Curry includes Cream (allergen)
(4, 7);  -- Chicken Curry includes Chicken

-- Insert mock data for `admin_week_recipes`
INSERT INTO admin_week_recipes (recipe_id, admin_week_id) VALUES
(1, 1),
(2, 1),
(3, 1),
(4, 1);  -- Bind all recipes to the admin week

-- Insert mock data for `selection_recipe`
INSERT INTO selection_recipe (recipe_id, selection_id) VALUES
(1, 1),
(2, 1),
(3, 1),
(4, 1);  -- Bind all recipes to the customer week


select * from recipe;

drop trigger trg_check_recipe_in_admin_week on selection_recipe;
drop trigger trigger_update_recipe on recipes_with_ingredients;
drop trigger trg_check_cancel_date on selection;
drop trigger trg_update_cancel_date on subscription;
drop function update_recipe_on_ingredient_add();
drop function check_recipe_in_admin_week();
drop function check_cancel_date_before_insert();
drop function update_cancel_date();
drop table selection_recipe;
drop table admin_week_recipes;
drop table recipes_with_ingredients;
drop table ingredient_type;
drop table type;
drop table ingredient;
drop table recipe;
drop table selection;
drop table admin_week;
drop table subscription;
drop table customer_information;
drop table registration;
drop table address;
drop table city;
drop table country;
