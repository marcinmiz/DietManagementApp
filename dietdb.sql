-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 19 Lip 2021, 22:41
-- Wersja serwera: 10.4.11-MariaDB
-- Wersja PHP: 7.4.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `dietdb`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `category`
--

CREATE TABLE `category` (
  `category_id` bigint(20) NOT NULL,
  `category_name` varchar(255) COLLATE utf8_polish_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `category`
--

INSERT INTO `category` (`category_id`, `category_name`) VALUES
(1, 'Fruit'),
(2, 'Vegetables'),
(3, 'Dairy');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(195);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `nutrient`
--

CREATE TABLE `nutrient` (
  `nutrient_id` bigint(20) NOT NULL,
  `nutrient_name` varchar(255) COLLATE utf8_polish_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `nutrient`
--

INSERT INTO `nutrient` (`nutrient_id`, `nutrient_name`) VALUES
(1, 'Protein'),
(2, 'Carbohydrates'),
(3, 'Fat'),
(4, 'Salt'),
(5, 'Sodium');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `password_reset_token`
--

CREATE TABLE `password_reset_token` (
  `id` bigint(20) NOT NULL,
  `expiry_date` datetime DEFAULT NULL,
  `token` varchar(255) COLLATE utf8_polish_ci DEFAULT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `product`
--

CREATE TABLE `product` (
  `product_id` bigint(20) NOT NULL,
  `calories` varchar(255) COLLATE utf8_polish_ci DEFAULT NULL,
  `creation_date` datetime DEFAULT NULL,
  `product_name` varchar(255) COLLATE utf8_polish_ci DEFAULT NULL,
  `category_category_id` bigint(20) DEFAULT NULL,
  `product_favourite` bit(1) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `approval_status` varchar(255) COLLATE utf8_polish_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `product`
--

INSERT INTO `product` (`product_id`, `calories`, `creation_date`, `product_name`, `category_category_id`, `product_favourite`, `owner_id`, `approval_status`) VALUES
(30, '2236', '2021-07-11 17:36:01', 'Paprica', 2, b'0', 1, 'pending'),
(178, '20', '2021-07-17 13:01:50', 'Banana', 1, b'0', 1, 'pending');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `product_nutrients`
--

CREATE TABLE `product_nutrients` (
  `product_nutrient_id` bigint(20) NOT NULL,
  `nutrient_amount` double DEFAULT NULL,
  `nutrient_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `product_nutrients`
--

INSERT INTO `product_nutrients` (`product_nutrient_id`, `nutrient_amount`, `nutrient_id`, `product_id`) VALUES
(31, 5, 1, 30),
(32, 2, 2, 30),
(33, 3, 4, 30),
(34, 4, 3, 30),
(46, 0, 1, 45),
(47, 0, 3, 45),
(48, 0, 2, 45),
(49, 0, 4, 45),
(51, 0, 3, 50),
(52, 0, 1, 50),
(53, 0, 2, 50),
(54, 0, 4, 50),
(56, 4, 1, 55),
(57, 0, 2, 55),
(58, 0, 4, 55),
(59, 0, 3, 55),
(66, 15, 1, 65),
(67, 23, 2, 65),
(69, 15, 1, 68),
(70, 23, 2, 68),
(77, 40, 2, 76),
(78, 0, 4, 76),
(79, 58, 3, 76),
(80, 30, 1, 76),
(132, 0, 2, 131),
(133, 0, 1, 131),
(134, 0, 4, 131),
(135, 0, 3, 131),
(137, 0, 2, 136),
(138, 0, 4, 136),
(139, 0, 1, 136),
(140, 0, 3, 136),
(179, 45, 2, 178),
(180, 45, 1, 178),
(181, 7, 3, 178),
(182, 45, 4, 178);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `recipe`
--

CREATE TABLE `recipe` (
  `recipe_id` bigint(20) NOT NULL,
  `approval_status` bit(1) NOT NULL,
  `creation_date` datetime DEFAULT NULL,
  `recipe_name` varchar(255) COLLATE utf8_polish_ci DEFAULT NULL,
  `shared` bit(1) NOT NULL,
  `owner_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `recipe`
--

INSERT INTO `recipe` (`recipe_id`, `approval_status`, `creation_date`, `recipe_name`, `shared`, `owner_id`) VALUES
(1, b'0', '2021-07-05 06:59:16', 'Cheesecake', b'0', 1);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `recipe_product`
--

CREATE TABLE `recipe_product` (
  `quantity` double DEFAULT NULL,
  `unit` varchar(255) COLLATE utf8_polish_ci DEFAULT NULL,
  `recipe_recipe_id` bigint(20) NOT NULL,
  `product_product_id` bigint(20) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `role`
--

CREATE TABLE `role` (
  `role_id` int(11) NOT NULL,
  `name` varchar(255) COLLATE utf8_polish_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `role`
--

INSERT INTO `role` (`role_id`, `name`) VALUES
(1, 'ROLE_ADMIN');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `user`
--

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `first_name` varchar(255) COLLATE utf8_polish_ci DEFAULT NULL,
  `google_name` varchar(255) COLLATE utf8_polish_ci DEFAULT NULL,
  `google_username` varchar(255) COLLATE utf8_polish_ci DEFAULT NULL,
  `last_name` varchar(255) COLLATE utf8_polish_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_polish_ci DEFAULT NULL,
  `phone` varchar(255) COLLATE utf8_polish_ci DEFAULT NULL,
  `username` varchar(255) COLLATE utf8_polish_ci DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `user`
--

INSERT INTO `user` (`id`, `enabled`, `first_name`, `google_name`, `google_username`, `last_name`, `password`, `phone`, `username`) VALUES
(1, b'0', 'Max', NULL, NULL, 'Maximowicz', '1234', NULL, 'Max');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `user_role`
--

CREATE TABLE `user_role` (
  `user_role_id` bigint(20) NOT NULL,
  `role_role_id` int(11) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `user_role`
--

INSERT INTO `user_role` (`user_role_id`, `role_role_id`, `user_id`) VALUES
(1, 1, 1);

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`category_id`);

--
-- Indeksy dla tabeli `nutrient`
--
ALTER TABLE `nutrient`
  ADD PRIMARY KEY (`nutrient_id`);

--
-- Indeksy dla tabeli `password_reset_token`
--
ALTER TABLE `password_reset_token`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK5lwtbncug84d4ero33v3cfxvl` (`user_id`);

--
-- Indeksy dla tabeli `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`product_id`),
  ADD KEY `FKle1pobdrc8a2uw97gukfmvan4` (`category_category_id`),
  ADD KEY `FKn3le0a5l0wmq4gs20rn3ty36x` (`owner_id`);

--
-- Indeksy dla tabeli `product_nutrients`
--
ALTER TABLE `product_nutrients`
  ADD PRIMARY KEY (`product_nutrient_id`),
  ADD KEY `FK9hhl9e28afwsxcc6phw7429fw` (`nutrient_id`),
  ADD KEY `FKqha8lu59px0d52qk7wcvcj9x` (`product_id`);

--
-- Indeksy dla tabeli `recipe`
--
ALTER TABLE `recipe`
  ADD PRIMARY KEY (`recipe_id`),
  ADD KEY `FKjuqaa5jj2ukh9k520hs9yuuwd` (`owner_id`);

--
-- Indeksy dla tabeli `recipe_product`
--
ALTER TABLE `recipe_product`
  ADD PRIMARY KEY (`product_product_id`,`recipe_recipe_id`),
  ADD KEY `FKkscvvh1xw47dbj743t6vhmc5d` (`recipe_recipe_id`);

--
-- Indeksy dla tabeli `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`role_id`);

--
-- Indeksy dla tabeli `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `user_role`
--
ALTER TABLE `user_role`
  ADD PRIMARY KEY (`user_role_id`),
  ADD KEY `FKotxvofgf4qtsunbe0i3vhady6` (`role_role_id`),
  ADD KEY `FK859n2jvi8ivhui0rl0esws6o` (`user_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
