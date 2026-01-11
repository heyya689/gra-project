-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 11, 2026 at 06:40 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `gra_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `biznes`
--

CREATE TABLE `biznes` (
  `biznes_id` int(11) NOT NULL,
  `emri` varchar(100) NOT NULL,
  `pershkrim` text DEFAULT NULL,
  `kategori` varchar(50) DEFAULT NULL,
  `nipt` varchar(20) NOT NULL,
  `license` varchar(50) DEFAULT NULL,
  `telefon` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `website` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `biznes`
--

INSERT INTO `biznes` (`biznes_id`, `emri`, `pershkrim`, `kategori`, `nipt`, `license`, `telefon`, `email`, `website`, `created_at`, `updated_at`) VALUES
(1, 'Zoo Tirana', '', 'MUZE', 'K123456789', '', '', 'zoo@gra.com', NULL, '2025-12-08 20:57:14', '2025-12-08 20:57:14'),
(2, 'Kafe UPT', 'Kafe', 'KAFENE', '12345', '', '', 'kejsi@fti.edu.al', NULL, '2025-12-09 08:35:43', '2025-12-09 08:35:43'),
(3, 'cat cafe', '', 'KAFENE', '123', '', '', '1234', NULL, '2026-01-08 14:17:03', '2026-01-08 14:17:03'),
(5, 'Relax', '', 'SPA', '9876', '', '123', '12345678910', NULL, '2026-01-08 14:39:15', '2026-01-08 14:39:15'),
(6, 'Millenium', '', 'KINEMA', '1111', '', '111', '666', NULL, '2026-01-08 14:43:38', '2026-01-08 14:43:38'),
(7, 'GYM TANI', '', 'FITNES', 'QWERTY', '', '11', '11111', NULL, '2026-01-08 14:50:36', '2026-01-08 14:50:36'),
(8, '4G', '', 'KAFENE', 'asdf', '', '', '', NULL, '2026-01-08 14:54:47', '2026-01-08 14:54:47'),
(9, 'Cineplex', '', 'KINEMA', 'zxcvb', '', '', '', NULL, '2026-01-08 15:04:28', '2026-01-08 15:04:28'),
(10, 'Sleep', '', 'SPA', 'poiuy', '', '', '', NULL, '2026-01-08 15:07:17', '2026-01-08 15:07:17');

-- --------------------------------------------------------

--
-- Table structure for table `biznes_imazhe`
--

CREATE TABLE `biznes_imazhe` (
  `imazh_id` int(11) NOT NULL,
  `biznes_id` int(11) NOT NULL,
  `url` varchar(500) NOT NULL,
  `pershkrim` varchar(255) DEFAULT NULL,
  `is_primary` tinyint(1) DEFAULT 0,
  `renditja` int(11) DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `biznes_kategori`
--

CREATE TABLE `biznes_kategori` (
  `biznes_id` int(11) NOT NULL,
  `kategori_id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `biznes_kategori`
--

INSERT INTO `biznes_kategori` (`biznes_id`, `kategori_id`, `created_at`) VALUES
(2, 3, '2026-01-08 15:21:39'),
(7, 7, '2026-01-08 15:21:39'),
(8, 3, '2026-01-08 15:21:39');

-- --------------------------------------------------------

--
-- Table structure for table `biznes_lokacion`
--

CREATE TABLE `biznes_lokacion` (
  `biznes_id` int(11) NOT NULL,
  `lokacion_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `faq`
--

CREATE TABLE `faq` (
  `faq_id` int(11) NOT NULL,
  `pyetje` text NOT NULL,
  `pergjigje` text NOT NULL,
  `renditja` int(11) DEFAULT 0,
  `is_active` tinyint(1) DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `faqja_kategori`
--

CREATE TABLE `faqja_kategori` (
  `kategori_id` int(11) NOT NULL,
  `emri` varchar(100) NOT NULL,
  `pershkrim` text DEFAULT NULL,
  `renditja` int(11) DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `faqja_kategori`
--

INSERT INTO `faqja_kategori` (`kategori_id`, `emri`, `pershkrim`, `renditja`, `created_at`) VALUES
(1, 'GJENERAL', 'Pyetje të përgjithshme për sistemin', 0, '2025-12-08 17:43:33'),
(2, 'REZERVIMET', 'Pyetje rreth rezervimeve', 0, '2025-12-08 17:43:33'),
(3, 'PAGESAT', 'Pyetje rreth pagesave', 0, '2025-12-08 17:43:33'),
(4, 'LLOGARIA', 'Pyetje rreth menaxhimit të llogarisë', 0, '2025-12-08 17:43:33'),
(5, 'BIZNES', 'Pyetje për bizneset', 0, '2025-12-08 17:43:33');

-- --------------------------------------------------------

--
-- Table structure for table `faq_kategori`
--

CREATE TABLE `faq_kategori` (
  `faq_id` int(11) NOT NULL,
  `kategori_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `inventari`
--

CREATE TABLE `inventari` (
  `inventar_id` int(11) NOT NULL,
  `biznes_id` int(11) NOT NULL,
  `emer_produkt` varchar(100) NOT NULL,
  `pershkrim` text DEFAULT NULL,
  `sasi` int(11) DEFAULT 0,
  `cmimi` decimal(10,2) DEFAULT NULL,
  `njesia` varchar(20) DEFAULT NULL,
  `kategoria` varchar(50) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `kategori`
--

CREATE TABLE `kategori` (
  `kategori_id` int(11) NOT NULL,
  `emri` varchar(100) NOT NULL,
  `ikona` varchar(50) DEFAULT NULL,
  `pershkrim` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kategori`
--

INSERT INTO `kategori` (`kategori_id`, `emri`, `ikona`, `pershkrim`, `created_at`) VALUES
(1, 'RESTORANT', '🍽️', 'Restorante ', '2025-12-08 17:43:33'),
(2, 'HOTEL', '🏨', 'Hotele dhe akomodime', '2025-12-08 17:43:33'),
(3, 'KAFENE', '☕', 'Kafene dhe bar', '2025-12-08 17:43:33'),
(4, 'MUZE', '🏛️', 'Muze dhe atraksione kulturore', '2025-12-08 17:43:33'),
(5, 'KINEMA', '🎬', 'Kinema dhe teatër', '2025-12-08 17:43:33'),
(6, 'SPA', '💆', 'Spa dhe qendra relaksimi', '2025-12-08 17:43:33'),
(7, 'FITNES', '💪', 'Palestra dhe qendra sportive', '2025-12-08 17:43:33'),
(8, 'BAKERY', '🥐', 'Furra ', '2025-12-08 17:43:33');

-- --------------------------------------------------------

--
-- Table structure for table `kontakt`
--

CREATE TABLE `kontakt` (
  `kontakt_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `email` varchar(100) NOT NULL,
  `subjekti` varchar(200) NOT NULL,
  `mesazh` text NOT NULL,
  `status` enum('PENDING','READ','REPLIED','CLOSED') DEFAULT 'PENDING',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `lokacion`
--

CREATE TABLE `lokacion` (
  `lokacion_id` int(11) NOT NULL,
  `qyteti` varchar(100) NOT NULL,
  `adresa` varchar(255) DEFAULT NULL,
  `rruga` varchar(100) DEFAULT NULL,
  `numri` varchar(20) DEFAULT NULL,
  `zip_code` varchar(10) DEFAULT NULL,
  `latitude` decimal(10,8) DEFAULT NULL,
  `longitude` decimal(11,8) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `notifikime`
--

CREATE TABLE `notifikime` (
  `njoftim_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `titulli` varchar(100) NOT NULL,
  `mesazh` text NOT NULL,
  `tipi` enum('REZERVIM','PAGESE','SISTEM','PROMOCION','LAJME') DEFAULT 'SISTEM',
  `lexuar` tinyint(1) DEFAULT 0,
  `data` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pagesat`
--

CREATE TABLE `pagesat` (
  `pagesa_id` int(11) NOT NULL,
  `rezervim_id` int(11) NOT NULL,
  `shuma` decimal(10,2) NOT NULL,
  `metoda` enum('CARD','CASH','BANK_TRANSFER','WALLET') DEFAULT 'CARD',
  `status` enum('PENDING','COMPLETED','FAILED','REFUNDED') DEFAULT 'PENDING',
  `transaction_id` varchar(100) DEFAULT NULL,
  `payment_date` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pagesat_detaje`
--

CREATE TABLE `pagesat_detaje` (
  `detaje_id` int(11) NOT NULL,
  `pagesa_id` int(11) NOT NULL,
  `reference` varchar(100) DEFAULT NULL,
  `card_last_four` varchar(4) DEFAULT NULL,
  `card_type` varchar(20) DEFAULT NULL,
  `payment_gateway` varchar(50) DEFAULT NULL,
  `gateway_response` text DEFAULT NULL,
  `ip_address` varchar(45) DEFAULT NULL,
  `user_agent` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pagesat_historik`
--

CREATE TABLE `pagesat_historik` (
  `historik_id` int(11) NOT NULL,
  `pagesa_id` int(11) NOT NULL,
  `status` varchar(50) DEFAULT NULL,
  `mesazh` text DEFAULT NULL,
  `data` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `preferenca`
--

CREATE TABLE `preferenca` (
  `preferenca_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `njoftime_aktive` tinyint(1) DEFAULT 1,
  `gjuha` varchar(10) DEFAULT 'sq',
  `tema` varchar(20) DEFAULT 'light',
  `email_notifications` tinyint(1) DEFAULT 1,
  `sms_notifications` tinyint(1) DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `rezervim`
--

CREATE TABLE `rezervim` (
  `rezervim_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `biznes_id` int(11) NOT NULL,
  `inventar_id` int(11) DEFAULT NULL,
  `data` datetime NOT NULL,
  `numri_personave` int(11) DEFAULT 1,
  `shenime` varchar(255) DEFAULT NULL,
  `status` enum('PENDING','CONFIRMED','CANCELLED','COMPLETED') DEFAULT 'PENDING',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rezervim`
--

INSERT INTO `rezervim` (`rezervim_id`, `user_id`, `biznes_id`, `inventar_id`, `data`, `numri_personave`, `shenime`, `status`, `created_at`, `updated_at`) VALUES
(1, 4, 1, NULL, '2025-12-10 03:30:00', 4, 'allergic to animals', 'CONFIRMED', '2025-12-09 08:25:03', '2025-12-09 08:25:17'),
(2, 1, 2, NULL, '2020-12-11 00:00:00', 1, '', 'CONFIRMED', '2025-12-09 08:47:01', '2025-12-09 08:47:17'),
(3, 2, 3, NULL, '2026-05-16 12:30:00', 4, '', 'CONFIRMED', '2026-01-08 14:26:07', '2026-01-08 14:26:18');

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE `role` (
  `role_id` int(11) NOT NULL,
  `emri` varchar(50) NOT NULL,
  `description` text DEFAULT NULL,
  `permissions` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`role_id`, `emri`, `description`, `permissions`, `created_at`) VALUES
(1, 'ADMIN', 'Administrator i plotë i sistemit', 'ALL', '2025-12-08 17:43:33'),
(2, 'BIZNES', 'Pronar ose menaxher biznesi', 'MANAGE_BUSINESS,VIEW_REPORTS', '2025-12-08 17:43:33'),
(3, 'KLIENT', 'Përdorues i rregullt', 'MAKE_RESERVATIONS,WRITE_REVIEWS', '2025-12-08 17:43:33');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `role` enum('CLIENT','BUSINESS','ADMIN') DEFAULT 'CLIENT',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `name`, `email`, `password`, `phone`, `role`, `created_at`) VALUES
(1, 'Gloria Bylykbashi', 'gloria@gra.com', '$2a$10$ABC123...', NULL, 'CLIENT', '2025-12-08 17:32:12'),
(2, 'Kejda Abdullari', 'kejda@gra.com', '$2a$10$XYZ456...', NULL, 'ADMIN', '2025-12-08 17:32:12'),
(3, 'Kejsi Gjikolaj', 'kejsi@gra.com', '$2a$10$DEF789...', NULL, 'BUSINESS', '2025-12-08 17:32:12'),
(4, 'Raina Stërmasi', 'raina@gra.com', '$2a$10$GHI012...', NULL, 'CLIENT', '2025-12-08 17:32:12'),
(5, 'Klient Test', 'test@test.com', '$2a$10$test...', NULL, 'CLIENT', '2025-12-08 17:32:12'),
(7, 'Test User', 'test@gra.com', 'test123', NULL, 'CLIENT', '2025-12-08 18:52:56'),
(15, 'Test User', '1765268788026@gra.com', 'test123', NULL, 'CLIENT', '2025-12-09 08:26:29'),
(17, 'test', '123', '123', NULL, 'CLIENT', '2026-01-08 14:15:56');

-- --------------------------------------------------------

--
-- Table structure for table `user_role`
--

CREATE TABLE `user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  `assigned_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `vleresim`
--

CREATE TABLE `vleresim` (
  `vleresim_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `biznes_id` int(11) NOT NULL,
  `rating` int(11) DEFAULT NULL CHECK (`rating` between 1 and 5),
  `koment` text DEFAULT NULL,
  `is_approved` tinyint(1) DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `vleresim`
--

INSERT INTO `vleresim` (`vleresim_id`, `user_id`, `biznes_id`, `rating`, `koment`, `is_approved`, `created_at`, `updated_at`) VALUES
(1, 2, 1, 1, 'Ska sherbim klienti', 1, '2025-12-09 08:23:03', '2025-12-09 08:23:30');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `biznes`
--
ALTER TABLE `biznes`
  ADD PRIMARY KEY (`biznes_id`),
  ADD UNIQUE KEY `nipt` (`nipt`),
  ADD KEY `idx_biznes_emri` (`emri`);

--
-- Indexes for table `biznes_imazhe`
--
ALTER TABLE `biznes_imazhe`
  ADD PRIMARY KEY (`imazh_id`),
  ADD KEY `biznes_id` (`biznes_id`);

--
-- Indexes for table `biznes_kategori`
--
ALTER TABLE `biznes_kategori`
  ADD PRIMARY KEY (`biznes_id`,`kategori_id`),
  ADD KEY `kategori_id` (`kategori_id`);

--
-- Indexes for table `biznes_lokacion`
--
ALTER TABLE `biznes_lokacion`
  ADD PRIMARY KEY (`biznes_id`,`lokacion_id`),
  ADD KEY `lokacion_id` (`lokacion_id`);

--
-- Indexes for table `faq`
--
ALTER TABLE `faq`
  ADD PRIMARY KEY (`faq_id`);

--
-- Indexes for table `faqja_kategori`
--
ALTER TABLE `faqja_kategori`
  ADD PRIMARY KEY (`kategori_id`),
  ADD UNIQUE KEY `emri` (`emri`);

--
-- Indexes for table `faq_kategori`
--
ALTER TABLE `faq_kategori`
  ADD PRIMARY KEY (`faq_id`,`kategori_id`),
  ADD KEY `kategori_id` (`kategori_id`);

--
-- Indexes for table `inventari`
--
ALTER TABLE `inventari`
  ADD PRIMARY KEY (`inventar_id`),
  ADD KEY `biznes_id` (`biznes_id`);

--
-- Indexes for table `kategori`
--
ALTER TABLE `kategori`
  ADD PRIMARY KEY (`kategori_id`),
  ADD UNIQUE KEY `emri` (`emri`);

--
-- Indexes for table `kontakt`
--
ALTER TABLE `kontakt`
  ADD PRIMARY KEY (`kontakt_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `lokacion`
--
ALTER TABLE `lokacion`
  ADD PRIMARY KEY (`lokacion_id`);

--
-- Indexes for table `notifikime`
--
ALTER TABLE `notifikime`
  ADD PRIMARY KEY (`njoftim_id`),
  ADD KEY `idx_notifikime_user` (`user_id`,`lexuar`);

--
-- Indexes for table `pagesat`
--
ALTER TABLE `pagesat`
  ADD PRIMARY KEY (`pagesa_id`),
  ADD KEY `rezervim_id` (`rezervim_id`);

--
-- Indexes for table `pagesat_detaje`
--
ALTER TABLE `pagesat_detaje`
  ADD PRIMARY KEY (`detaje_id`),
  ADD KEY `pagesa_id` (`pagesa_id`);

--
-- Indexes for table `pagesat_historik`
--
ALTER TABLE `pagesat_historik`
  ADD PRIMARY KEY (`historik_id`),
  ADD KEY `pagesa_id` (`pagesa_id`);

--
-- Indexes for table `preferenca`
--
ALTER TABLE `preferenca`
  ADD PRIMARY KEY (`preferenca_id`),
  ADD UNIQUE KEY `user_id` (`user_id`);

--
-- Indexes for table `rezervim`
--
ALTER TABLE `rezervim`
  ADD PRIMARY KEY (`rezervim_id`),
  ADD KEY `inventar_id` (`inventar_id`),
  ADD KEY `idx_rezervim_user` (`user_id`),
  ADD KEY `idx_rezervim_biznes` (`biznes_id`),
  ADD KEY `idx_rezervim_status` (`status`);

--
-- Indexes for table `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`role_id`),
  ADD UNIQUE KEY `emri` (`emri`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `idx_user_email` (`email`);

--
-- Indexes for table `user_role`
--
ALTER TABLE `user_role`
  ADD PRIMARY KEY (`user_id`,`role_id`),
  ADD KEY `role_id` (`role_id`);

--
-- Indexes for table `vleresim`
--
ALTER TABLE `vleresim`
  ADD PRIMARY KEY (`vleresim_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `idx_vleresim_biznes` (`biznes_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `biznes`
--
ALTER TABLE `biznes`
  MODIFY `biznes_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `biznes_imazhe`
--
ALTER TABLE `biznes_imazhe`
  MODIFY `imazh_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `faq`
--
ALTER TABLE `faq`
  MODIFY `faq_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `faqja_kategori`
--
ALTER TABLE `faqja_kategori`
  MODIFY `kategori_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `inventari`
--
ALTER TABLE `inventari`
  MODIFY `inventar_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `kategori`
--
ALTER TABLE `kategori`
  MODIFY `kategori_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `kontakt`
--
ALTER TABLE `kontakt`
  MODIFY `kontakt_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `lokacion`
--
ALTER TABLE `lokacion`
  MODIFY `lokacion_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `notifikime`
--
ALTER TABLE `notifikime`
  MODIFY `njoftim_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pagesat`
--
ALTER TABLE `pagesat`
  MODIFY `pagesa_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pagesat_detaje`
--
ALTER TABLE `pagesat_detaje`
  MODIFY `detaje_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pagesat_historik`
--
ALTER TABLE `pagesat_historik`
  MODIFY `historik_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `preferenca`
--
ALTER TABLE `preferenca`
  MODIFY `preferenca_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `rezervim`
--
ALTER TABLE `rezervim`
  MODIFY `rezervim_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `role`
--
ALTER TABLE `role`
  MODIFY `role_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `vleresim`
--
ALTER TABLE `vleresim`
  MODIFY `vleresim_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `biznes_imazhe`
--
ALTER TABLE `biznes_imazhe`
  ADD CONSTRAINT `biznes_imazhe_ibfk_1` FOREIGN KEY (`biznes_id`) REFERENCES `biznes` (`biznes_id`) ON DELETE CASCADE;

--
-- Constraints for table `biznes_kategori`
--
ALTER TABLE `biznes_kategori`
  ADD CONSTRAINT `biznes_kategori_ibfk_1` FOREIGN KEY (`biznes_id`) REFERENCES `biznes` (`biznes_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `biznes_kategori_ibfk_2` FOREIGN KEY (`kategori_id`) REFERENCES `kategori` (`kategori_id`) ON DELETE CASCADE;

--
-- Constraints for table `biznes_lokacion`
--
ALTER TABLE `biznes_lokacion`
  ADD CONSTRAINT `biznes_lokacion_ibfk_1` FOREIGN KEY (`biznes_id`) REFERENCES `biznes` (`biznes_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `biznes_lokacion_ibfk_2` FOREIGN KEY (`lokacion_id`) REFERENCES `lokacion` (`lokacion_id`) ON DELETE CASCADE;

--
-- Constraints for table `faq_kategori`
--
ALTER TABLE `faq_kategori`
  ADD CONSTRAINT `faq_kategori_ibfk_1` FOREIGN KEY (`faq_id`) REFERENCES `faq` (`faq_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `faq_kategori_ibfk_2` FOREIGN KEY (`kategori_id`) REFERENCES `faqja_kategori` (`kategori_id`) ON DELETE CASCADE;

--
-- Constraints for table `inventari`
--
ALTER TABLE `inventari`
  ADD CONSTRAINT `inventari_ibfk_1` FOREIGN KEY (`biznes_id`) REFERENCES `biznes` (`biznes_id`) ON DELETE CASCADE;

--
-- Constraints for table `kontakt`
--
ALTER TABLE `kontakt`
  ADD CONSTRAINT `kontakt_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `notifikime`
--
ALTER TABLE `notifikime`
  ADD CONSTRAINT `notifikime_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `pagesat`
--
ALTER TABLE `pagesat`
  ADD CONSTRAINT `pagesat_ibfk_1` FOREIGN KEY (`rezervim_id`) REFERENCES `rezervim` (`rezervim_id`);

--
-- Constraints for table `pagesat_detaje`
--
ALTER TABLE `pagesat_detaje`
  ADD CONSTRAINT `pagesat_detaje_ibfk_1` FOREIGN KEY (`pagesa_id`) REFERENCES `pagesat` (`pagesa_id`) ON DELETE CASCADE;

--
-- Constraints for table `pagesat_historik`
--
ALTER TABLE `pagesat_historik`
  ADD CONSTRAINT `pagesat_historik_ibfk_1` FOREIGN KEY (`pagesa_id`) REFERENCES `pagesat` (`pagesa_id`) ON DELETE CASCADE;

--
-- Constraints for table `preferenca`
--
ALTER TABLE `preferenca`
  ADD CONSTRAINT `preferenca_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `rezervim`
--
ALTER TABLE `rezervim`
  ADD CONSTRAINT `rezervim_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `rezervim_ibfk_2` FOREIGN KEY (`biznes_id`) REFERENCES `biznes` (`biznes_id`),
  ADD CONSTRAINT `rezervim_ibfk_3` FOREIGN KEY (`inventar_id`) REFERENCES `inventari` (`inventar_id`);

--
-- Constraints for table `user_role`
--
ALTER TABLE `user_role`
  ADD CONSTRAINT `user_role_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `user_role_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE CASCADE;

--
-- Constraints for table `vleresim`
--
ALTER TABLE `vleresim`
  ADD CONSTRAINT `vleresim_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `vleresim_ibfk_2` FOREIGN KEY (`biznes_id`) REFERENCES `biznes` (`biznes_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
