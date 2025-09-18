INSERT INTO roles (role) VALUES
('ROLE_CUSTOMER'),
('ROLE_MANAGER');

INSERT INTO users (id, email, first_name, last_name, password, telegram_chat_id, is_deleted)
VALUES
(1, 'customer@example.com', 'Ivan', 'Petrov', 'hashed_password1', 123456789, false),
(2, 'manager@example.com', 'Olga', 'Ivanova', 'hashed_password2', 987654321, false);

INSERT INTO users_roles (user_id, role_id) VALUES
(1, 1),
(2, 2);