-- phpMyAdmin SQL Dump
-- version 3.1.3.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jan 22, 2011 at 01:41 PM
-- Server version: 5.1.50
-- PHP Version: 5.3.5

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Database: `osiris`
--

-- --------------------------------------------------------

--
-- Table structure for table `contacts`
--

CREATE TABLE IF NOT EXISTS `contacts` (
  `player_id` int(11) NOT NULL,
  `friends` longtext NOT NULL,
  `ignores` longtext NOT NULL,
  PRIMARY KEY (`player_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `contacts`
--

INSERT INTO `contacts` (`player_id`, `friends`, `ignores`) VALUES
(1, '[]', '[]');

-- --------------------------------------------------------

--
-- Table structure for table `items`
--

CREATE TABLE IF NOT EXISTS `items` (
  `player_id` int(11) NOT NULL,
  `inventory_item_ids` longtext NOT NULL,
  `inventory_item_amounts` longtext NOT NULL,
  `bank_item_ids` longtext NOT NULL,
  `bank_item_amounts` longtext NOT NULL,
  `equipment_item_ids` longtext NOT NULL,
  `equipment_item_amounts` longtext NOT NULL,
  PRIMARY KEY (`player_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `items`
--

INSERT INTO `items` (`player_id`, `inventory_item_ids`, `inventory_item_amounts`, `bank_item_ids`, `bank_item_amounts`, `equipment_item_ids`, `equipment_item_amounts`) VALUES
(1, '[995, 1042, 1042, 1042, 1042, 1042, 1042, 1042, 1042, 1042, 1042, 1042, 1042, 1042, 1042, 1042, 1042, 1042, 1042, 1042, 1042, 1042, 1042, 1042, 1042, 1042, 1042, 1042]', '[2147000000, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]', '[-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1]', '[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]', '[-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1]', '[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]');

-- --------------------------------------------------------

--
-- Table structure for table `players`
--

CREATE TABLE IF NOT EXISTS `players` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(12) NOT NULL,
  `password` varchar(40) NOT NULL,
  `ip` varchar(15) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0',
  `salt` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `players`
--

INSERT INTO `players` (`id`, `name`, `password`, `ip`, `status`, `salt`) VALUES
(1, 'boomer', '6ff132356667a5c33784466834891e3a3e76075a', '127.0.0.1', 2, -932341131);

-- --------------------------------------------------------

--
-- Table structure for table `settings`
--

CREATE TABLE IF NOT EXISTS `settings` (
  `player_id` int(11) NOT NULL,
  `position_x` int(11) NOT NULL,
  `position_y` int(11) NOT NULL,
  `position_z` int(11) NOT NULL,
  `run_energy` int(11) NOT NULL,
  `special_energy` int(11) NOT NULL,
  `attack_style` int(11) NOT NULL,
  `spell_book` varchar(10) NOT NULL,
  `gender` tinyint(4) NOT NULL,
  `auto_retaliate` tinyint(4) NOT NULL,
  `emote_status` longtext NOT NULL,
  PRIMARY KEY (`player_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `settings`
--

INSERT INTO `settings` (`player_id`, `position_x`, `position_y`, `position_z`, `run_energy`, `special_energy`, `attack_style`, `spell_book`, `gender`, `auto_retaliate`, `emote_status`) VALUES
(1, 3081, 3416, 0, 100, 1000, 0, 'NORMAL', 0, 0, '[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]');

-- --------------------------------------------------------

--
-- Table structure for table `skills`
--

CREATE TABLE IF NOT EXISTS `skills` (
  `player_id` int(11) NOT NULL DEFAULT '0',
  `attack_curlevel` int(11) NOT NULL,
  `attack_maxlevel` int(11) NOT NULL,
  `attack_xp` double(255,30) NOT NULL,
  `defence_curlevel` int(11) NOT NULL,
  `defence_maxlevel` int(11) NOT NULL,
  `defence_xp` double(255,30) NOT NULL,
  `strength_curlevel` int(11) NOT NULL,
  `strength_maxlevel` int(11) NOT NULL,
  `strength_xp` double(255,30) NOT NULL,
  `hitpoints_curlevel` int(11) NOT NULL,
  `hitpoints_maxlevel` int(11) NOT NULL,
  `hitpoints_xp` double(255,30) NOT NULL,
  `range_curlevel` int(11) NOT NULL,
  `range_maxlevel` int(11) NOT NULL,
  `range_xp` double(255,30) NOT NULL,
  `prayer_curlevel` int(11) NOT NULL,
  `prayer_maxlevel` int(11) NOT NULL,
  `prayer_xp` double(255,30) NOT NULL,
  `magic_curlevel` int(11) NOT NULL,
  `magic_maxlevel` int(11) NOT NULL,
  `magic_xp` double(255,30) NOT NULL,
  `cooking_curlevel` int(11) NOT NULL,
  `cooking_maxlevel` int(11) NOT NULL,
  `cooking_xp` double(255,30) NOT NULL,
  `woodcutting_curlevel` int(11) NOT NULL,
  `woodcutting_maxlevel` int(11) NOT NULL,
  `woodcutting_xp` double(255,30) NOT NULL,
  `fletching_curlevel` int(11) NOT NULL,
  `fletching_maxlevel` int(11) NOT NULL,
  `fletching_xp` double(255,30) NOT NULL,
  `fishing_curlevel` int(11) NOT NULL,
  `fishing_maxlevel` int(11) NOT NULL,
  `fishing_xp` double(255,30) NOT NULL,
  `firemaking_curlevel` int(11) NOT NULL,
  `firemaking_maxlevel` int(11) NOT NULL,
  `firemaking_xp` double(255,30) NOT NULL,
  `crafting_curlevel` int(11) NOT NULL,
  `crafting_maxlevel` int(11) NOT NULL,
  `crafting_xp` double(255,30) NOT NULL,
  `smithing_curlevel` int(11) NOT NULL,
  `smithing_maxlevel` int(11) NOT NULL,
  `smithing_xp` double(255,30) NOT NULL,
  `mining_curlevel` int(11) NOT NULL,
  `mining_maxlevel` int(11) NOT NULL,
  `mining_xp` double(255,30) NOT NULL,
  `herblore_curlevel` int(11) NOT NULL,
  `herblore_maxlevel` int(11) NOT NULL,
  `herblore_xp` double(255,30) NOT NULL,
  `agility_curlevel` int(11) NOT NULL,
  `agility_maxlevel` int(11) NOT NULL,
  `agility_xp` double(255,30) NOT NULL,
  `thieving_curlevel` int(11) NOT NULL,
  `thieving_maxlevel` int(11) NOT NULL,
  `thieving_xp` double(255,30) NOT NULL,
  `slayer_curlevel` int(11) NOT NULL,
  `slayer_maxlevel` int(11) NOT NULL,
  `slayer_xp` double(255,30) NOT NULL,
  `farming_curlevel` int(11) NOT NULL,
  `farming_maxlevel` int(11) NOT NULL,
  `farming_xp` double(255,30) NOT NULL,
  `runecrafting_curlevel` int(11) NOT NULL,
  `runecrafting_maxlevel` int(11) NOT NULL,
  `runecrafting_xp` double(255,30) NOT NULL,
  `construction_curlevel` int(11) NOT NULL,
  `construction_maxlevel` int(11) NOT NULL,
  `construction_xp` double(255,30) NOT NULL,
  `hunter_curlevel` int(11) NOT NULL,
  `hunter_maxlevel` int(11) NOT NULL,
  `hunter_xp` double(255,30) NOT NULL,
  `summoning_curlevel` int(11) NOT NULL,
  `summoning_maxlevel` int(11) NOT NULL,
  `summoning_xp` double(255,30) NOT NULL,
  `overall_curlevel` int(11) NOT NULL,
  `overall_maxlevel` int(11) NOT NULL,
  `overall_xp` double(255,30) NOT NULL,
  PRIMARY KEY (`player_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `skills`
--

INSERT INTO `skills` (`player_id`, `attack_curlevel`, `attack_maxlevel`, `attack_xp`, `defence_curlevel`, `defence_maxlevel`, `defence_xp`, `strength_curlevel`, `strength_maxlevel`, `strength_xp`, `hitpoints_curlevel`, `hitpoints_maxlevel`, `hitpoints_xp`, `range_curlevel`, `range_maxlevel`, `range_xp`, `prayer_curlevel`, `prayer_maxlevel`, `prayer_xp`, `magic_curlevel`, `magic_maxlevel`, `magic_xp`, `cooking_curlevel`, `cooking_maxlevel`, `cooking_xp`, `woodcutting_curlevel`, `woodcutting_maxlevel`, `woodcutting_xp`, `fletching_curlevel`, `fletching_maxlevel`, `fletching_xp`, `fishing_curlevel`, `fishing_maxlevel`, `fishing_xp`, `firemaking_curlevel`, `firemaking_maxlevel`, `firemaking_xp`, `crafting_curlevel`, `crafting_maxlevel`, `crafting_xp`, `smithing_curlevel`, `smithing_maxlevel`, `smithing_xp`, `mining_curlevel`, `mining_maxlevel`, `mining_xp`, `herblore_curlevel`, `herblore_maxlevel`, `herblore_xp`, `agility_curlevel`, `agility_maxlevel`, `agility_xp`, `thieving_curlevel`, `thieving_maxlevel`, `thieving_xp`, `slayer_curlevel`, `slayer_maxlevel`, `slayer_xp`, `farming_curlevel`, `farming_maxlevel`, `farming_xp`, `runecrafting_curlevel`, `runecrafting_maxlevel`, `runecrafting_xp`, `construction_curlevel`, `construction_maxlevel`, `construction_xp`, `hunter_curlevel`, `hunter_maxlevel`, `hunter_xp`, `summoning_curlevel`, `summoning_maxlevel`, `summoning_xp`, `overall_curlevel`, `overall_maxlevel`, `overall_xp`) VALUES
(1, 1, 1, 0.000000000000000000000000000000, 1, 1, 0.000000000000000000000000000000, 1, 1, 0.000000000000000000000000000000, 10, 10, 1154.000000000000000000000000000000, 1, 1, 0.000000000000000000000000000000, 1, 1, 0.000000000000000000000000000000, 1, 1, 0.000000000000000000000000000000, 1, 1, 0.000000000000000000000000000000, 1, 1, 0.000000000000000000000000000000, 1, 1, 0.000000000000000000000000000000, 1, 1, 0.000000000000000000000000000000, 1, 1, 0.000000000000000000000000000000, 1, 1, 0.000000000000000000000000000000, 1, 1, 0.000000000000000000000000000000, 1, 1, 0.000000000000000000000000000000, 1, 1, 0.000000000000000000000000000000, 1, 1, 0.000000000000000000000000000000, 1, 1, 0.000000000000000000000000000000, 1, 1, 0.000000000000000000000000000000, 1, 1, 0.000000000000000000000000000000, 1, 1, 0.000000000000000000000000000000, 1, 1, 0.000000000000000000000000000000, 1, 1, 0.000000000000000000000000000000, 1, 1, 0.000000000000000000000000000000, 33, 33, 1154.000000000000000000000000000000);
