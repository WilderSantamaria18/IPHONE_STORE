-- Crear la base de datos IPHONE_STORE
CREATE DATABASE iphone_store;
USE iphone_store;

-- ===========================================
-- TABLA CARGO (Departamentos de la empresa)
-- ===========================================
CREATE TABLE cargo (
    ID_Cargo INT PRIMARY KEY AUTO_INCREMENT,
    Nombre VARCHAR(50) NOT NULL UNIQUE,
    Descripcion VARCHAR(200)
);

-- ===========================================
-- TABLA USUARIO 
-- ===========================================
CREATE TABLE usuario (
    ID_Usuario INT PRIMARY KEY AUTO_INCREMENT,
    ID_Cargo INT NOT NULL,
    Nombres VARCHAR(100) NOT NULL,
    dni CHAR(8) NOT NULL UNIQUE,
    Email VARCHAR(100) NOT NULL UNIQUE,
    Username VARCHAR(50) NOT NULL UNIQUE,
    Contrasena VARCHAR(100) NOT NULL,
    Estado ENUM('Activo', 'Inactivo') DEFAULT 'Activo',
    FOREIGN KEY (ID_Cargo) REFERENCES cargo(ID_Cargo)
);

-- ===========================================
-- RESTO DE TABLAS
-- ===========================================
-- Crear la tabla Proveedor
CREATE TABLE proveedor (
    ID_Proveedor INT PRIMARY KEY AUTO_INCREMENT,
    Nombre VARCHAR(100) NOT NULL,
    ruc CHAR(11) NOT NULL UNIQUE,
    telefono CHAR(15) NOT NULL,
    Email VARCHAR(100) NOT NULL,
    Estado ENUM('Activo', 'Inactivo') DEFAULT 'Activo'
);

-- Crear la tabla Producto
CREATE TABLE producto (
    ID_Producto INT PRIMARY KEY AUTO_INCREMENT,
    ID_Proveedor INT NOT NULL,
    Modelo VARCHAR(100) NOT NULL,
    lanzamiento CHAR(4) NOT NULL,
    Procesador VARCHAR(50) NOT NULL,
    Ram VARCHAR(20) NOT NULL,
    Almacenamiento VARCHAR(20) NOT NULL,
    Precio_Venta DECIMAL(10, 2) NOT NULL,
    Precio_Costo DECIMAL(10, 2) NOT NULL,
    Stock INT NOT NULL DEFAULT 0,
    Imagen VARCHAR(500),
    Estado ENUM('Activo', 'Inactivo') DEFAULT 'Activo',
    FOREIGN KEY (ID_Proveedor) REFERENCES proveedor(ID_Proveedor)
);

-- Crear la tabla Cliente
CREATE TABLE cliente (
    ID_Cliente INT PRIMARY KEY AUTO_INCREMENT,
    Tipo_Cliente VARCHAR(50) NOT NULL,
    Nombre VARCHAR(100) NOT NULL,
    documento CHAR(11) UNIQUE,
    Direccion VARCHAR(200) NOT NULL,
    Telefono CHAR(9) NOT NULL,
    Email VARCHAR(100) NOT NULL,
    Estado ENUM('Activo', 'Inactivo') DEFAULT 'Activo'
);

-- Crear la tabla Pedido (SIN ESTADO - venta directa)
CREATE TABLE pedido (
    ID_Pedido INT PRIMARY KEY AUTO_INCREMENT,
    ID_Cliente INT NOT NULL,
    ID_Usuario INT NOT NULL, -- Usuario que registra el pedido
    Fecha_Pedido DATETIME DEFAULT CURRENT_TIMESTAMP,
    Fecha_Entrega DATE,
    Tipo_Pago VARCHAR(50),
    Total_Pagar DECIMAL(10, 2) DEFAULT 0.00,
    FOREIGN KEY (ID_Cliente) REFERENCES cliente(ID_Cliente),
    FOREIGN KEY (ID_Usuario) REFERENCES usuario(ID_Usuario)
);

-- Crear la tabla Detalle_Pedido
CREATE TABLE detalle_pedido (
    ID_Detalle INT PRIMARY KEY AUTO_INCREMENT,
    ID_Pedido INT NOT NULL,
    ID_Producto INT NOT NULL,
    Cantidad INT NOT NULL,
    Precio_Unitario DECIMAL(10, 2) NOT NULL,
    Subtotal DECIMAL(10, 2) GENERATED ALWAYS AS (Cantidad * Precio_Unitario) STORED,
    FOREIGN KEY (ID_Pedido) REFERENCES pedido(ID_Pedido) ON DELETE CASCADE,
    FOREIGN KEY (ID_Producto) REFERENCES producto(ID_Producto)
);

-- Crear la tabla Comprobante
CREATE TABLE comprobante (
    ID_Comprobante INT PRIMARY KEY AUTO_INCREMENT,
    ID_Pedido INT NOT NULL UNIQUE,
    ID_Cliente INT NOT NULL,
    ID_Usuario INT NOT NULL, -- Usuario que genera el comprobante
    Tipo_Pago VARCHAR(50) NOT NULL,
    Tipo_Comprobante VARCHAR(50) NOT NULL,
    Numero_Comprobante VARCHAR(50) NOT NULL UNIQUE,
    Subtotal DECIMAL(10, 2) NOT NULL,
    IGV DECIMAL(10, 2) NOT NULL,
    Total_Pagar DECIMAL(10, 2) NOT NULL,
    Fecha_Emision DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ID_Pedido) REFERENCES pedido(ID_Pedido),
    FOREIGN KEY (ID_Cliente) REFERENCES cliente(ID_Cliente),
    FOREIGN KEY (ID_Usuario) REFERENCES usuario(ID_Usuario)
);

-- Crear la tabla Detalle_Comprobante
CREATE TABLE detalle_comprobante (
    ID_Detalle_Comprobante INT PRIMARY KEY AUTO_INCREMENT,
    ID_Comprobante INT NOT NULL,
    ID_Producto INT NOT NULL,
    Cantidad INT NOT NULL,
    Precio_Unitario DECIMAL(10, 2) NOT NULL,
    Subtotal DECIMAL(10, 2) GENERATED ALWAYS AS (Cantidad * Precio_Unitario) STORED,
    FOREIGN KEY (ID_Comprobante) REFERENCES comprobante(ID_Comprobante) ON DELETE CASCADE,
    FOREIGN KEY (ID_Producto) REFERENCES producto(ID_Producto)
);

-- ===========================================
-- INSERTAR DATOS INICIALES
-- ===========================================

-- Insertar cargos/departamentos (SIN ESTADO)
INSERT INTO cargo (Nombre, Descripcion) VALUES 
('Administracion', 'Acceso completo a todas las funcionalidades del sistema'),
('Almacen', 'Gestión de productos, inventario y proveedores'),
('Ventas', 'Gestión de pedidos, clientes y generación de comprobantes');

-- Insertar usuarios de prueba
INSERT INTO usuario (ID_Cargo, Nombres, dni, Email, Username, Contrasena) VALUES 
(1, 'Wilder Rodriguez', '12345678', 'wilder@empresa.com', 'WILDER', '$2a$10$o2BnQhA.8oSuk1NrKl8SBeLixj5WuNahh4vtPcIJy/7J5hQwfXb6K'), -- 123
(3, 'Josias Martinez', '87654321', 'josias@empresa.com', 'JOSIAS', '$2a$10$5UY3LqKPdptbFNvKseA3W.bIz72Fr9dxn7TFkIMIpxT8jZW6v.L8.'), -- 456
(2, 'Yefferson Lopez', '11223344', 'yefferson@empresa.com', 'YEFFERSON', '$2a$10$sVRCjCPN5gkBQ4q/V7zqs.f0/ZuOASvgYUdcBX5kv6mbqlGvKmLqK'); -- 789

-- Datos de proveedores especializados en iPhone
INSERT INTO proveedor (Nombre, ruc, telefono, Email) VALUES 
('Apple Peru SAC', '20123456789', '987654321', 'ventas@appleperu.com'),
('Mac Center Peru', '20987654321', '987654322', 'distribucion@maccenter.pe'),
('iStore Peru', '20111222333', '987654323', 'contacto@istoreperu.com');

-- Datos de productos iPhone exclusivos con las imágenes proporcionadas
INSERT INTO producto (ID_Proveedor, Modelo, lanzamiento, Procesador, Ram, Almacenamiento, Precio_Venta, Precio_Costo, Stock, Imagen) VALUES 
(1, 'iPhone 16e', '2024', 'A18', '8GB', '128GB', 3999.00, 2800.00, 30, 'https://mac-center.com.pe/cdn/shop/files/83736d9f-dd3d-57a2-bcf4-2668b975f66d_m_jpg_1_e8ca3d0d-be75-43b5-bcfb-96cfb20cf5f3_550x.jpg?v=1739993688'),
(2, 'iPhone 16 Pro', '2024', 'A18 Pro', '8GB', '256GB', 4999.00, 3500.00, 25, 'https://mac-center.com.pe/cdn/shop/files/IMG-14858941_89bed05c-b26c-4d44-b154-4a1d3cf04bff_550x.jpg?v=1728067829'),
(3, 'iPhone 16', '2024', 'A18', '6GB', '128GB', 3999.00, 2800.00, 20, 'https://mac-center.com.pe/cdn/shop/files/IMG-14858811_571321c9-c609-487b-b5c2-a5a487f3305c_550x.jpg?v=1728067200'),
(2, 'iPhone 13', '2021', 'A15 Bionic', '4GB', '128GB', 2999.00, 2000.00, 15, 'https://mac-center.com.pe/cdn/shop/files/iPhone_13_Midnight_PDP_Image_position-1A__CLCO_v1_83f068d3-1619-4e81-8f3a-cfb7d7ff73af_550x.jpg?v=1700286900');

-- Datos de ejemplo para clientes
INSERT INTO cliente (Tipo_Cliente, Nombre, documento, Direccion, Telefono, Email) VALUES 
('Natural', 'Juan Perez Garcia', '12345678', 'Av. Lima 123, San Isidro', '987654321', 'juan.perez@email.com'),
('Juridica', 'Tecnologia Movil SAC', '20123456789', 'Jr. Comercio 456, Miraflores', '987654322', 'ventas@tecnologiamovil.com'),
('Natural', 'Maria Rodriguez Lopez', '87654321', 'Av. Arequipa 789, Lima', '987654323', 'maria.rodriguez@gmail.com'),
('Natural', 'Carlos Mendoza Silva', '13243568', 'Jr. Union 321, San Borja', '987654324', 'carlos.mendoza@hotmail.com');

ALTER TABLE pedido ADD COLUMN Estado ENUM('Emitido','Anulado') DEFAULT 'Emitido';
ALTER TABLE comprobante ADD COLUMN Estado ENUM('Emitido','Anulado') DEFAULT 'Emitido';