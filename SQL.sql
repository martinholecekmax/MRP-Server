-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 12, 2018 at 03:03 AM
-- Server version: 10.1.28-MariaDB
-- PHP Version: 7.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `smtp`
--
CREATE DATABASE IF NOT EXISTS `smtp` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `smtp`;

-- --------------------------------------------------------

--
-- Table structure for table `mailboxes`
--

CREATE TABLE `mailboxes` (
  `MailboxID` int(11) NOT NULL,
  `Mailbox` varchar(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `Domain` varchar(64) CHARACTER SET latin1 COLLATE latin1_bin NOT NULL,
  `Mailgroup` varchar(256) DEFAULT NULL,
  `Password` varchar(64) NOT NULL,
  `Token` varchar(64) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `mailboxes`
--

INSERT INTO `mailboxes` (`MailboxID`, `Mailbox`, `Domain`, `Mailgroup`, `Password`, `Token`) VALUES
(1, 'martin.holecek', 'derby.ac.uk', 'pass: university', 'ADBA6C0EC8A8D89EFB03DE642427A09302FA4F7474989ECF33FD545A30D2FF5B', '7fff1bc5-26c7-4a31-b533-b550908a5840');

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE `messages` (
  `MessageID` int(11) NOT NULL,
  `MailboxID` int(11) NOT NULL,
  `UID` int(11) NOT NULL,
  `Subject` varchar(256) DEFAULT NULL,
  `Sender` varchar(256) NOT NULL,
  `Recipient` varchar(25600) NOT NULL,
  `Date` date NOT NULL,
  `Mime` longtext,
  `Body` text NOT NULL,
  `Flag` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `messages`
--

INSERT INTO `messages` (`MessageID`, `MailboxID`, `UID`, `Subject`, `Sender`, `Recipient`, `Date`, `Mime`, `Body`, `Flag`) VALUES
(41, 1, 1, 'Global Game Jam!', 'tonny@microsoft.com', 's.hatfield@derby.ac.uk', '2007-12-08', NULL, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas fringilla ipsum tortor, eget blandit turpis imperdiet finibus. Donec posuere, sapien at facilisis ullamcorper, orci orci aliquam odio, vel laoreet nunc lectus sed metus. Nam eget diam quis ante condimentum venenatis suscipit eu ipsum. Donec posuere vitae nisi ac auctor. Etiam tempus luctus lobortis. In pretium urna eu ligula fermentum vulputate. Praesent facilisis id ipsum nec volutpat. Cras dignissim lacus at mi egestas mattis. Curabitur tempus risus at nisi faucibus, vel dignissim urna scelerisque. Curabitur sed elementum elit, ac convallis orci. Aliquam efficitur erat quis felis ultricies, at efficitur libero aliquet.', 'DELETED'),
(42, 2, 43, 'Access Security and Your Student Card', 'c.windmill@derby.ac.uk', 'r.j.self@derby.ac.uk', '2002-11-21', NULL, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas fringilla ipsum tortor, eget blandit turpis imperdiet finibus. Donec posuere, sapien at facilisis ullamcorper, orci orci aliquam odio, vel laoreet nunc lectus sed metus. Nam eget diam quis ante condimentum venenatis suscipit eu ipsum. Donec posuere vitae nisi ac auctor. Etiam tempus luctus lobortis. In pretium urna eu ligula fermentum vulputate. Praesent facilisis id ipsum nec volutpat. Cras dignissim lacus at mi egestas mattis. Curabitur tempus risus at nisi faucibus, vel dignissim urna scelerisque. Curabitur sed elementum elit, ac convallis orci. Aliquam efficitur erat quis felis ultricies, at efficitur libero aliquet.', 'SEEN'),
(43, 2, 532, 'We\'ve Moved!', 'd.voorhis@derby.ac.uk', 'bob@google.com', '2017-11-10', NULL, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas fringilla ipsum tortor, eget blandit turpis imperdiet finibus. Donec posuere, sapien at facilisis ullamcorper, orci orci aliquam odio, vel laoreet nunc lectus sed metus. Nam eget diam quis ante condimentum venenatis suscipit eu ipsum. Donec posuere vitae nisi ac auctor. Etiam tempus luctus lobortis. In pretium urna eu ligula fermentum vulputate. Praesent facilisis id ipsum nec volutpat. Cras dignissim lacus at mi egestas mattis. Curabitur tempus risus at nisi faucibus, vel dignissim urna scelerisque. Curabitur sed elementum elit, ac convallis orci. Aliquam efficitur erat quis felis ultricies, at efficitur libero aliquet.', 'DELETED'),
(44, 2, 63, 'Programming Exercises for the Summer', 'w.rippin@derby.ac.uk', 'tonny@microsoft.com', '2015-12-21', NULL, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas fringilla ipsum tortor, eget blandit turpis imperdiet finibus. Donec posuere, sapien at facilisis ullamcorper, orci orci aliquam odio, vel laoreet nunc lectus sed metus. Nam eget diam quis ante condimentum venenatis suscipit eu ipsum. Donec posuere vitae nisi ac auctor. Etiam tempus luctus lobortis. In pretium urna eu ligula fermentum vulputate. Praesent facilisis id ipsum nec volutpat. Cras dignissim lacus at mi egestas mattis. Curabitur tempus risus at nisi faucibus, vel dignissim urna scelerisque. Curabitur sed elementum elit, ac convallis orci. Aliquam efficitur erat quis felis ultricies, at efficitur libero aliquet.', 'SEEN'),
(46, 1, 2, 'Last Minute Reminders', 'bob@google.com', 'w.rippin@derby.ac.uk', '2008-12-22', NULL, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas fringilla ipsum tortor, eget blandit turpis imperdiet finibus. Donec posuere, sapien at facilisis ullamcorper, orci orci aliquam odio, vel laoreet nunc lectus sed metus. Nam eget diam quis ante condimentum venenatis suscipit eu ipsum. Donec posuere vitae nisi ac auctor. Etiam tempus luctus lobortis. In pretium urna eu ligula fermentum vulputate. Praesent facilisis id ipsum nec volutpat. Cras dignissim lacus at mi egestas mattis. Curabitur tempus risus at nisi faucibus, vel dignissim urna scelerisque. Curabitur sed elementum elit, ac convallis orci. Aliquam efficitur erat quis felis ultricies, at efficitur libero aliquet.', 'SEEN'),
(47, 1, 3, 'Phase 2 Marking', 'maxim@centrum.cz', 'c.windmill@derby.ac.uk', '2009-01-12', NULL, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas fringilla ipsum tortor, eget blandit turpis imperdiet finibus. Donec posuere, sapien at facilisis ullamcorper, orci orci aliquam odio, vel laoreet nunc lectus sed metus. Nam eget diam quis ante condimentum venenatis suscipit eu ipsum. Donec posuere vitae nisi ac auctor. Etiam tempus luctus lobortis. In pretium urna eu ligula fermentum vulputate. Praesent facilisis id ipsum nec volutpat. Cras dignissim lacus at mi egestas mattis. Curabitur tempus risus at nisi faucibus, vel dignissim urna scelerisque. Curabitur sed elementum elit, ac convallis orci. Aliquam efficitur erat quis felis ultricies, at efficitur libero aliquet.', 'SENT'),
(48, 1, 4, 'Make a change!', 'bob@google.com', 'students@derby.ac.uk', '2010-10-22', NULL, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas fringilla ipsum tortor, eget blandit turpis imperdiet finibus. Donec posuere, sapien at facilisis ullamcorper, orci orci aliquam odio, vel laoreet nunc lectus sed metus. Nam eget diam quis ante condimentum venenatis suscipit eu ipsum. Donec posuere vitae nisi ac auctor. Etiam tempus luctus lobortis. In pretium urna eu ligula fermentum vulputate. Praesent facilisis id ipsum nec volutpat. Cras dignissim lacus at mi egestas mattis. Curabitur tempus risus at nisi faucibus, vel dignissim urna scelerisque. Curabitur sed elementum elit, ac convallis orci. Aliquam efficitur erat quis felis ultricies, at efficitur libero aliquet.', 'DRAFT'),
(49, 1, 5, 'Referral - Update 2', 'tonny@microsoft.com', 'o.bagdasar@derby.ac.uk', '2010-12-08', NULL, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas fringilla ipsum tortor, eget blandit turpis imperdiet finibus. Donec posuere, sapien at facilisis ullamcorper, orci orci aliquam odio, vel laoreet nunc lectus sed metus. Nam eget diam quis ante condimentum venenatis suscipit eu ipsum. Donec posuere vitae nisi ac auctor. Etiam tempus luctus lobortis. In pretium urna eu ligula fermentum vulputate. Praesent facilisis id ipsum nec volutpat. Cras dignissim lacus at mi egestas mattis. Curabitur tempus risus at nisi faucibus, vel dignissim urna scelerisque. Curabitur sed elementum elit, ac convallis orci. Aliquam efficitur erat quis felis ultricies, at efficitur libero aliquet.', 'SENT'),
(51, 1, 6, 'Game Jam!', 'bob@google.com', 'd.voorhis@derby.ac.uk', '2011-01-23', NULL, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas fringilla ipsum tortor, eget blandit turpis imperdiet finibus. Donec posuere, sapien at facilisis ullamcorper, orci orci aliquam odio, vel laoreet nunc lectus sed metus. Nam eget diam quis ante condimentum venenatis suscipit eu ipsum. Donec posuere vitae nisi ac auctor. Etiam tempus luctus lobortis. In pretium urna eu ligula fermentum vulputate. Praesent facilisis id ipsum nec volutpat. Cras dignissim lacus at mi egestas mattis. Curabitur tempus risus at nisi faucibus, vel dignissim urna scelerisque. Curabitur sed elementum elit, ac convallis orci. Aliquam efficitur erat quis felis ultricies, at efficitur libero aliquet.', 'SEEN'),
(52, 1, 7, 'Opinion Poll - Last Chance!', 'maxim@centrum.cz', 'm.mcintosh2@unimail.derby.ac.uk', '2011-05-12', NULL, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas fringilla ipsum tortor, eget blandit turpis imperdiet finibus. Donec posuere, sapien at facilisis ullamcorper, orci orci aliquam odio, vel laoreet nunc lectus sed metus. Nam eget diam quis ante condimentum venenatis suscipit eu ipsum. Donec posuere vitae nisi ac auctor. Etiam tempus luctus lobortis. In pretium urna eu ligula fermentum vulputate. Praesent facilisis id ipsum nec volutpat. Cras dignissim lacus at mi egestas mattis. Curabitur tempus risus at nisi faucibus, vel dignissim urna scelerisque. Curabitur sed elementum elit, ac convallis orci. Aliquam efficitur erat quis felis ultricies, at efficitur libero aliquet.', 'SENT'),
(53, 1, 8, 'Bring paper and pen/pencil!', 'bob@google.com', 'd.voorhis@derby.ac.uk', '2011-08-02', NULL, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas fringilla ipsum tortor, eget blandit turpis imperdiet finibus. Donec posuere, sapien at facilisis ullamcorper, orci orci aliquam odio, vel laoreet nunc lectus sed metus. Nam eget diam quis ante condimentum venenatis suscipit eu ipsum. Donec posuere vitae nisi ac auctor. Etiam tempus luctus lobortis. In pretium urna eu ligula fermentum vulputate. Praesent facilisis id ipsum nec volutpat. Cras dignissim lacus at mi egestas mattis. Curabitur tempus risus at nisi faucibus, vel dignissim urna scelerisque. Curabitur sed elementum elit, ac convallis orci. Aliquam efficitur erat quis felis ultricies, at efficitur libero aliquet.', 'SEEN'),
(54, 1, 9, 'Graphics tutorial', 'tonny@microsoft.com', 'w.rippin@derby.ac.uk', '2011-10-08', NULL, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas fringilla ipsum tortor, eget blandit turpis imperdiet finibus. Donec posuere, sapien at facilisis ullamcorper, orci orci aliquam odio, vel laoreet nunc lectus sed metus. Nam eget diam quis ante condimentum venenatis suscipit eu ipsum. Donec posuere vitae nisi ac auctor. Etiam tempus luctus lobortis. In pretium urna eu ligula fermentum vulputate. Praesent facilisis id ipsum nec volutpat. Cras dignissim lacus at mi egestas mattis. Curabitur tempus risus at nisi faucibus, vel dignissim urna scelerisque. Curabitur sed elementum elit, ac convallis orci. Aliquam efficitur erat quis felis ultricies, at efficitur libero aliquet.', 'DRAFT'),
(56, 1, 10, 'Accessing Watson Analytics', 'bob@google.com', 'r.j.self@derby.ac.uk', '2012-12-22', NULL, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas fringilla ipsum tortor, eget blandit turpis imperdiet finibus. Donec posuere, sapien at facilisis ullamcorper, orci orci aliquam odio, vel laoreet nunc lectus sed metus. Nam eget diam quis ante condimentum venenatis suscipit eu ipsum. Donec posuere vitae nisi ac auctor. Etiam tempus luctus lobortis. In pretium urna eu ligula fermentum vulputate. Praesent facilisis id ipsum nec volutpat. Cras dignissim lacus at mi egestas mattis. Curabitur tempus risus at nisi faucibus, vel dignissim urna scelerisque. Curabitur sed elementum elit, ac convallis orci. Aliquam efficitur erat quis felis ultricies, at efficitur libero aliquet.', 'SEEN'),
(57, 1, 11, 'Library Services', 'maxim@centrum.cz', 'd.voorhis@derby.ac.uk', '2013-10-12', NULL, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas fringilla ipsum tortor, eget blandit turpis imperdiet finibus. Donec posuere, sapien at facilisis ullamcorper, orci orci aliquam odio, vel laoreet nunc lectus sed metus. Nam eget diam quis ante condimentum venenatis suscipit eu ipsum. Donec posuere vitae nisi ac auctor. Etiam tempus luctus lobortis. In pretium urna eu ligula fermentum vulputate. Praesent facilisis id ipsum nec volutpat. Cras dignissim lacus at mi egestas mattis. Curabitur tempus risus at nisi faucibus, vel dignissim urna scelerisque. Curabitur sed elementum elit, ac convallis orci. Aliquam efficitur erat quis felis ultricies, at efficitur libero aliquet.', 'SENT'),
(58, 1, 12, 'Updating Constructors', 'bob@google.com', 'w.rippin@derby.ac.uk', '2017-07-22', NULL, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas fringilla ipsum tortor, eget blandit turpis imperdiet finibus. Donec posuere, sapien at facilisis ullamcorper, orci orci aliquam odio, vel laoreet nunc lectus sed metus. Nam eget diam quis ante condimentum venenatis suscipit eu ipsum. Donec posuere vitae nisi ac auctor. Etiam tempus luctus lobortis. In pretium urna eu ligula fermentum vulputate. Praesent facilisis id ipsum nec volutpat. Cras dignissim lacus at mi egestas mattis. Curabitur tempus risus at nisi faucibus, vel dignissim urna scelerisque. Curabitur sed elementum elit, ac convallis orci. Aliquam efficitur erat quis felis ultricies, at efficitur libero aliquet.', 'SEEN'),
(59, 1, 13, 'Code Demonstration and Viva', 'tonny@microsoft.com', 'c.windmill@derby.ac.uk', '2017-10-08', NULL, 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas fringilla ipsum tortor, eget blandit turpis imperdiet finibus. Donec posuere, sapien at facilisis ullamcorper, orci orci aliquam odio, vel laoreet nunc lectus sed metus. Nam eget diam quis ante condimentum venenatis suscipit eu ipsum. Donec posuere vitae nisi ac auctor. Etiam tempus luctus lobortis. In pretium urna eu ligula fermentum vulputate. Praesent facilisis id ipsum nec volutpat. Cras dignissim lacus at mi egestas mattis. Curabitur tempus risus at nisi faucibus, vel dignissim urna scelerisque. Curabitur sed elementum elit, ac convallis orci. Aliquam efficitur erat quis felis ultricies, at efficitur libero aliquet.', 'RECENT');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `mailboxes`
--
ALTER TABLE `mailboxes`
  ADD PRIMARY KEY (`MailboxID`);

--
-- Indexes for table `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`MessageID`),
  ADD KEY `FK_MAILBOX` (`MailboxID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `mailboxes`
--
ALTER TABLE `mailboxes`
  MODIFY `MailboxID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `messages`
--
ALTER TABLE `messages`
  MODIFY `MessageID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=60;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `FK_MAILBOX` FOREIGN KEY (`MailboxID`) REFERENCES `mailboxes` (`MailboxID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
