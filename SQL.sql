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
(1, 'max', 'derby.ac.uk', 'people', '9BAF3A40312F39849F46DAD1040F2F039F1CFFA1238C41E9DB675315CFAD39B6', 'ef25baf0-db36-4cbf-9016-742456bf4481'),
(2, 'martin.holecek', 'derby.ac.uk', 'pass: university', 'ADBA6C0EC8A8D89EFB03DE642427A09302FA4F7474989ECF33FD545A30D2FF5B', '7fff1bc5-26c7-4a31-b533-b550908a5840');

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
(41, 1, 1, 'Global Game Jam!', 'tonny@microsoft.com', 's.hatfield@derby.ac.uk', '2007-12-08', NULL, 'Many of the potential mentors are previous Derby graduates and are keen to support you on your career journey.', 'DELETED'),
(42, 2, 43, 'Access Security and Your Student Card', 'c.windmill@derby.ac.uk', 'r.j.self@derby.ac.uk', '2002-11-21', NULL, 'We have been able to gain access to one of the most modern analytical systems that is provided by the IBM Watson division called Watson Analytics. All students in the University of Derby can now sign up to the system and gain free access whilst they are studying at the university of Derby using the details in the attached document which also provides links to training materials and examples of what it can do.', 'SEEN'),
(43, 2, 532, 'We\'ve Moved!', 'd.voorhis@derby.ac.uk', 'bob@google.com', '2017-11-10', NULL, 'You\'ve hopefully heard that Computing and Mathematics have moved from Kedleston Road to Markeaton Street, and Electronics have new offices. We’re all in or near the new STEM area of the main Markeaton Street building.', 'DELETED'),
(44, 2, 63, 'Programming Exercises for the Summer', 'w.rippin@derby.ac.uk', 'tonny@microsoft.com', '2015-12-21', NULL, 'It is important that you continue to practice programming and not just stop for the summer, especially if you are on Computer Science or Computer Games Programming.  Past experience has shown us that students who do not continue programming over the summer tend to do poorly in the second year.   Besides, if you are on Computer Science or Computer Games Programming, we would expect you to enjoy programming and WANT to do more of it!', 'SEEN'),
(46, 1, 2, 'Last Minute Reminders', 'bob@google.com', 'w.rippin@derby.ac.uk', '2008-12-22', NULL, 'Given that you are approaching the submission for Graphics 1, I would just like to remind you of a few things you need to do when you submit your assignment.   You can think of this as a check list.   These are all listed in the assignment specification so there is nothing new here, but it is easy to forget some of these so please check that you have done everything carefully.  You do not want to lose at least 20% of your grade simply because of failing to follow a simple instruction.  If you have already submitted and missed one of these points, you still have time to correct it and resubmit - I will only mark the last one submitted.', 'SEEN'),
(47, 1, 3, 'Phase 2 Marking', 'maxim@centrum.cz', 'c.windmill@derby.ac.uk', '2009-01-12', NULL, 'MS318, 319 have been booked for your phase 2 submissions on the 15th of January from 9am-5pm.\r\nYour time slots are mapped as follows:\r\nMonday 9-11 : 9am - 10:30am\r\nMonday 3-5   : 10:30am - 12noon\r\nLunch             : 12noon - 12:30pm\r\nMonday 5-7  : 12:30pm - 2pm\r\nTuesday 2-4 : 2pm - 3:30pm\r\nFriday 9-11   : 3:30pm- 5pm\r\n\r\nIf you have an extension for phase 1 you will demonstrate your code for phase 1 in this slot.\r\n', 'SENT'),
(48, 1, 4, 'Make a change!', 'bob@google.com', 'students@derby.ac.uk', '2010-10-22', NULL, 'Welcome to your December InTouch! As this semester comes to an end we hope you’ve had some awesome experiences.', 'DRAFT'),
(49, 1, 5, 'Referral - Update 2', 'tonny@microsoft.com', 'o.bagdasar@derby.ac.uk', '2010-12-08', NULL, 'Dear students, This message only concerns students with CM Referrals. \r\nAfter the CM Referral Lab test today, two students have already ensured a pass mark.  \r\nTo pass the CM Referral please ensure that (av. of 4 tests + test mark)/2 >=40%.\r\nHowever, so far, only 8 out of 23 students engaged with their referrals.\r\n', 'SENT'),
(51, 1, 6, 'Game Jam!', 'bob@google.com', 'd.voorhis@derby.ac.uk', '2011-01-23', NULL, 'Hazel Glasse, Subject Librarian for the College of Engineering and Technology will be running a library support session on Thursday 25th January from 2-3pm at Britannia Mill Library. This will be an opportunity for students to drop in and ask any questions they have about finding information for their assignments or dissertations or how to use the use their subject-specific e-resources. ', 'SEEN'),
(52, 1, 7, 'Opinion Poll - Last Chance!', 'maxim@centrum.cz', 'm.mcintosh2@unimail.derby.ac.uk', '2011-05-12', NULL, 'Hi Everyone, Last chance to submit your feedback before your course reps. meet this afternoon.\r\n', 'SENT'),
(53, 1, 8, 'Bring paper and pen/pencil!', 'bob@google.com', 'd.voorhis@derby.ac.uk', '2011-08-02', NULL, 'Please bring a pen or pencil to tonight\'s (Nov 28) lecture, because it\'s time to fill out the official, paper-based, module evaluation survey forms!', 'SEEN'),
(54, 1, 9, 'Graphics tutorial', 'tonny@microsoft.com', 'w.rippin@derby.ac.uk', '2011-10-08', NULL, 'The second computer-based test for Graphics 1 will take place in the tutorials this week, i.e. in the tutorial at 9am on Wednesday 29th November or 1pm on Friday 1st December.\r\nOnce again, there are 15 questions and you will have 20 minutes to do them.   It is a closed-book test so you will not be allowed to use your notes or access any sources on the Internet.   You can use a calculator if you wish to, including the Windows calculator.   This test is worth 15% of your module grade.\r\n', 'DRAFT'),
(56, 1, 10, 'Accessing Watson Analytics', 'bob@google.com', 'r.j.self@derby.ac.uk', '2012-12-22', NULL, 'Many of you will be producing mobile apps for oyur Team Projects next semester.\r\nAs you consider the development of Mobile Apps, you must ensure that you consider this report from the UK Information Commissioners Office (ICO)\r\n', 'SEEN'),
(57, 1, 11, 'Library Services', 'maxim@centrum.cz', 'd.voorhis@derby.ac.uk', '2013-10-12', NULL, 'There will be a session in BM310/11 on Placements for 2nd year Computer Science (and CGP) students on Monday November 27th from 1pm until 2pm.  Some students will be in lectures until 1pm, so we will start a few minutes after 1pm to accommodate the walk between sites.', 'SENT'),
(58, 1, 12, 'Updating Constructors', 'bob@google.com', 'w.rippin@derby.ac.uk', '2017-07-22', NULL, 'Due to personal reasons, I am afraid I need to leave early today.  However, Neall will still be in the tutorial between 1pm and 3pm, so please ask him if you need any help.', 'SEEN'),
(59, 1, 13, 'Code Demonstration and Viva', 'tonny@microsoft.com', 'c.windmill@derby.ac.uk', '2017-10-08', NULL, 'Please remember that your phase 1 submission is due at the end of week 11 with the viva in week 12 lab slots - if you have issues with the implementation of RFC 821 client to MTA communication or similar please do ensure that you are coming to the lab slots to make use of contact time.', 'RECENT');

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
