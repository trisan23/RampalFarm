USE farm2home;

-- Categories (5)
INSERT INTO categories (category_id, category_name, description) VALUES
(1, 'Vegetables', 'Fresh organic vegetables'),
(2, 'Fruits', 'Seasonal fruits'),
(3, 'Dairy', 'Milk and dairy products'),
(4, 'Grains', 'Rice, wheat and grains'),
(5, 'Spices', 'Organic spices');

-- Users (10)
INSERT INTO users (user_id, username, email, password, phone_number, role, status) VALUES
(1, 'Admin User', 'admin@farm.com', 'admin123', '9800000001', 'admin', 'active'),
(2, 'Ram Sharma', 'ram@gmail.com', 'ram123', '9800000002', 'customer', 'active'),
(3, 'Sita Gurung', 'sita@gmail.com', 'sita123', '9800000003', 'customer', 'active'),
(4, 'Hari Karki', 'hari@gmail.com', 'hari123', '9800000004', 'customer', 'active'),
(5, 'Gita Rai', 'gita@gmail.com', 'gita123', '9800000005', 'customer', 'active'),
(6, 'Shyam Thapa', 'shyam@gmail.com', 'shyam123', '9800000006', 'customer', 'active'),
(7, 'Ramesh KC', 'ramesh@gmail.com', 'ramesh123', '9800000007', 'customer', 'active'),
(8, 'Anita Lama', 'anita@gmail.com', 'anita123', '9800000008', 'customer', 'active'),
(9, 'Bikash Poudel', 'bikash@gmail.com', 'bikash123', '9800000009', 'customer', 'active'),
(10, 'Sunita Magar', 'sunita@gmail.com', 'sunita123', '9800000010', 'customer', 'active');

-- Products (49)
INSERT INTO products (product_id, product_name, description, stock_quantity, price, image_url, category_id) VALUES
(1, 'Tomato', 'Fresh red tomato', 100, 120.00, 'images/products/tomato.png', 1),
(2, 'Potato', 'Organic potato', 200, 60.00, 'images/products/potato.png', 1),
(3, 'Spinach', 'Green leafy vegetable', 80, 40.00, 'images/products/spinach.png', 1),
(4, 'Carrot', 'Fresh carrots', 90, 70.00, 'images/products/carrot.png', 1),
(5, 'Apple', 'Sweet apple', 150, 250.00, 'images/products/apple.png', 2),
(6, 'Banana', 'Fresh banana', 180, 140.00, 'images/products/banana.png', 2),
(7, 'Orange', 'Juicy orange', 120, 180.00, 'images/products/orange.png', 2),
(8, 'Milk 1L', 'Cow milk', 80, 90.00, 'images/products/milk.png', 3),
(9, 'Paneer', 'Fresh paneer', 60, 350.00, 'images/products/paneer.png', 3),
(10, 'Rice 1kg', 'Basmati rice', 200, 180.00, 'images/products/rice.png', 4),
(11, 'Wheat 1kg', 'Organic wheat', 180, 120.00, 'images/products/wheat.png', 4),
(12, 'Turmeric', 'Organic turmeric', 70, 90.00, 'images/products/tumeric.png', 5),
(13, 'Cucumber', 'Crisp farm cucumber', 95, 55.00, 'images/products/cucumber.jpeg', 1),
(14, 'Cauliflower', 'Fresh white cauliflower', 65, 110.00, 'images/products/cauliflower.jpeg', 1),
(15, 'Curd 500g', 'Fresh homemade curd', 55, 130.00, 'images/products/curd.jpeg', 3),
(16, 'Lentils 1kg', 'Protein rich mixed lentils', 140, 220.00, 'images/products/lentils.jpeg', 4),
(17, 'Ginger', 'Fresh aromatic ginger', 85, 160.00, 'images/products/ginger.jpeg', 5),
(18, 'Broccoli', 'Fresh green broccoli', 50, 180.00, 'images/products/broccoli.jpeg', 1),
(19, 'Capsicum', 'Crisp green capsicum', 75, 95.00, 'images/products/capsicum.jpeg', 1),
(20, 'Papaya', 'Naturally sweet papaya', 60, 145.00, 'images/products/papaya.jpeg', 2),
(21, 'Guava', 'Fresh local guava', 90, 125.00, 'images/products/guava.jpeg', 2),
(22, 'Butter 250g', 'Farm fresh butter', 40, 240.00, 'images/products/butter.jpeg', 3),
(23, 'Mustard Seed', 'Pure mustard seed', 70, 310.00, 'images/products/mustard-seed.jpeg', 4),
(24, 'Cumin', 'Organic cumin seeds', 80, 175.00, 'images/products/cumin.jpeg', 5),
(25, 'Cabbage', 'Fresh green cabbage', 85, 65.00, 'images/products/cabbage.png', 1),
(26, 'Radish', 'Crisp white radish', 70, 45.00, 'images/products/radish.png', 1),
(27, 'Eggplant', 'Fresh purple eggplant', 80, 75.00, 'images/products/eggplant.png', 1),
(28, 'Bitter Gourd', 'Farm fresh bitter gourd', 60, 85.00, 'images/products/bitter-gourd.png', 1),
(29, 'Green Beans', 'Tender green beans', 90, 120.00, 'images/products/green-beans.png', 1),
(30, 'Mango', 'Sweet seasonal mango', 75, 220.00, 'images/products/mango.png', 2),
(31, 'Pomegranate', 'Fresh red pomegranate', 55, 280.00, 'images/products/pomegranate.png', 2),
(32, 'Pineapple', 'Juicy ripe pineapple', 45, 190.00, 'images/products/pineapple.png', 2),
(33, 'Grapes', 'Fresh seedless grapes', 65, 260.00, 'images/products/grapes.png', 2),
(34, 'Watermelon', 'Large fresh watermelon', 40, 95.00, 'images/products/watermelon.png', 2),
(35, 'Cheese 200g', 'Fresh dairy cheese', 45, 310.00, 'images/products/cheese.png', 3),
(36, 'Ghee 500ml', 'Pure cow ghee', 35, 650.00, 'images/products/ghee.png', 3),
(37, 'Khoya 250g', 'Traditional reduced milk khoya', 40, 330.00, 'images/products/khoya.png', 3),
(38, 'Lassi 500ml', 'Refreshing sweet lassi', 50, 120.00, 'images/products/lassi.png', 3),
(39, 'Cream 250ml', 'Rich fresh cream', 38, 260.00, 'images/products/cream.png', 3),
(40, 'Maize 1kg', 'Clean dried maize grains', 130, 110.00, 'images/products/maize.png', 4),
(41, 'Millet 1kg', 'Nutritious local millet', 110, 150.00, 'images/products/millet.png', 4),
(42, 'Buckwheat 1kg', 'Organic buckwheat grain', 95, 190.00, 'images/products/buckwheat.png', 4),
(43, 'Chickpeas 1kg', 'Protein rich chickpeas', 120, 210.00, 'images/products/chickpeas.png', 4),
(44, 'Kidney Beans 1kg', 'Fresh dried kidney beans', 105, 240.00, 'images/products/kidney-beans.png', 4),
(45, 'Coriander Powder', 'Aromatic coriander powder', 75, 140.00, 'images/products/coriander-powder.png', 5),
(46, 'Black Pepper', 'Whole black pepper', 55, 360.00, 'images/products/black-pepper.png', 5),
(47, 'Cardamom', 'Premium green cardamom', 35, 950.00, 'images/products/cardamom.png', 5),
(48, 'Cloves', 'Aromatic dried cloves', 45, 520.00, 'images/products/cloves.png', 5),
(49, 'Chili Powder', 'Spicy red chili powder', 70, 180.00, 'images/products/chili-powder.png', 5);

-- Orders (10)
INSERT INTO orders (order_id, user_id, total_amount, status, delivery_address) VALUES
(1, 2, 180.00, 'confirmed', 'Pokhara'),
(2, 3, 250.00, 'pending', 'Kathmandu'),
(3, 4, 300.00, 'shipped', 'Chitwan'),
(4, 5, 150.00, 'delivered', 'Butwal'),
(5, 6, 400.00, 'confirmed', 'Lalitpur'),
(6, 7, 220.00, 'pending', 'Bhaktapur'),
(7, 8, 500.00, 'shipped', 'Biratnagar'),
(8, 9, 350.00, 'delivered', 'Dharan'),
(9, 10, 275.00, 'confirmed', 'Janakpur'),
(10, 2, 600.00, 'pending', 'Pokhara');

-- Order Details (15)
INSERT INTO order_details (order_details_id, order_id, product_id, quantity, price_at_purchase) VALUES
(1, 1, 1, 1, 120.00),
(2, 1, 2, 1, 60.00),
(3, 2, 5, 1, 250.00),
(4, 3, 3, 2, 40.00),
(5, 3, 4, 1, 70.00),
(6, 4, 6, 1, 140.00),
(7, 5, 8, 2, 90.00),
(8, 6, 7, 1, 180.00),
(9, 6, 1, 1, 120.00),
(10, 7, 9, 1, 350.00),
(11, 8, 10, 1, 180.00),
(12, 8, 11, 1, 120.00),
(13, 9, 12, 2, 90.00),
(14, 10, 5, 2, 250.00),
(15, 10, 8, 1, 90.00);
