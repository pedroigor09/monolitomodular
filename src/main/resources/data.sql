INSERT INTO customers (name, email, phone) VALUES 
('João Silva', 'joao@example.com', '11987654321'),
('Maria Santos', 'maria@example.com', '11976543210'),
('Pedro Oliveira', 'pedro@example.com', '11965432109');


INSERT INTO orders (customer_id, created_at, status) VALUES 
(1, '2025-12-20T10:30:00', 'PENDING');

INSERT INTO order_items (order_id, product_name, quantity, unit_price) VALUES
(1, 'Notebook Dell', 1, 2500.00),
(1, 'Mouse Logitech', 2, 50.00);

INSERT INTO orders (customer_id, created_at, status) VALUES 
(2, '2025-12-21T14:20:00', 'CONFIRMED');

INSERT INTO order_items (order_id, product_name, quantity, unit_price) VALUES
(2, 'Teclado Mecânico', 1, 350.00),
(2, 'Monitor LG 24"', 1, 800.00),
(2, 'Webcam HD', 1, 200.00);

INSERT INTO orders (customer_id, created_at, status) VALUES 
(1, '2025-12-22T09:15:00', 'SHIPPED');

INSERT INTO order_items (order_id, product_name, quantity, unit_price) VALUES
(3, 'Headset Gamer', 1, 450.00);


