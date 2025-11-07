-- Inserting users with TEACHER role

set search_path to edu_service;

INSERT INTO users (id, name, email, role) VALUES
(1, 'John Smith', 'john.smith@school.edu', 'TEACHER'),
(2, 'Emily Johnson', 'emily.johnson@school.edu', 'TEACHER'),
(3, 'Michael Williams', 'michael.williams@school.edu', 'TEACHER'),
(4, 'Sarah Brown', 'sarah.brown@school.edu', 'TEACHER'),
(5, 'David Jones', 'david.jones@school.edu', 'TEACHER');

-- Inserting users with STUDENT role
INSERT INTO users (id, name, email, role) VALUES
(6, 'Alice Davis', 'alice.davis@student.edu', 'STUDENT'),
(7, 'Bob Miller', 'bob.miller@student.edu', 'STUDENT'),
(8, 'Carol Wilson', 'carol.wilson@student.edu', 'STUDENT'),
(9, 'Daniel Moore', 'daniel.moore@student.edu', 'STUDENT'),
(10, 'Eva Taylor', 'eva.taylor@student.edu', 'STUDENT'),
(11, 'Frank Anderson', 'frank.anderson@student.edu', 'STUDENT'),
(12, 'Grace Thomas', 'grace.thomas@student.edu', 'STUDENT'),
(13, 'Henry Jackson', 'henry.jackson@student.edu', 'STUDENT'),
(14, 'Iris White', 'iris.white@student.edu', 'STUDENT'),
(15, 'Jack Harris', 'jack.harris@student.edu', 'STUDENT'),
(16, 'Kate Martin', 'kate.martin@student.edu', 'STUDENT'),
(17, 'Liam Thompson', 'liam.thompson@student.edu', 'STUDENT'),
(18, 'Mia Garcia', 'mia.garcia@student.edu', 'STUDENT'),
(19, 'Nathan Martinez', 'nathan.martinez@student.edu', 'STUDENT'),
(20, 'Olivia Robinson', 'olivia.robinson@student.edu', 'STUDENT'),
(21, 'Paul Clark', 'paul.clark@student.edu', 'STUDENT'),
(22, 'Quinn Rodriguez', 'quinn.rodriguez@student.edu', 'STUDENT'),
(23, 'Rachel Lewis', 'rachel.lewis@student.edu', 'STUDENT'),
(24, 'Steve Lee', 'steve.lee@student.edu', 'STUDENT'),
(25, 'Tina Walker', 'tina.walker@student.edu', 'STUDENT'),
(26, 'Uma Hall', 'uma.hall@student.edu', 'STUDENT'),
(27, 'Victor Allen', 'victor.allen@student.edu', 'STUDENT'),
(28, 'Wendy Young', 'wendy.young@student.edu', 'STUDENT'),
(29, 'Xavier King', 'xavier.king@student.edu', 'STUDENT'),
(30, 'Yara Wright', 'yara.wright@student.edu', 'STUDENT'),
(31, 'Zack Scott', 'zack.scott@student.edu', 'STUDENT'),
(32, 'Ava Green', 'ava.green@student.edu', 'STUDENT'),
(33, 'Ben Baker', 'ben.baker@student.edu', 'STUDENT'),
(34, 'Cara Adams', 'cara.adams@student.edu', 'STUDENT'),
(35, 'Derek Nelson', 'derek.nelson@student.edu', 'STUDENT'),
(36, 'Elena Hill', 'elena.hill@student.edu', 'STUDENT'),
(37, 'Felix Ramirez', 'felix.ramirez@student.edu', 'STUDENT'),
(38, 'Gina Campbell', 'gina.campbell@student.edu', 'STUDENT'),
(39, 'Hugo Mitchell', 'hugo.mitchell@student.edu', 'STUDENT'),
(40, 'Ivy Roberts', 'ivy.roberts@student.edu', 'STUDENT'),
(41, 'Jake Carter', 'jake.carter@student.edu', 'STUDENT'),
(42, 'Kara Phillips', 'kara.phillips@student.edu', 'STUDENT'),
(43, 'Luna Evans', 'luna.evans@student.edu', 'STUDENT'),
(44, 'Max Turner', 'max.turner@student.edu', 'STUDENT'),
(45, 'Nina Torres', 'nina.torres@student.edu', 'STUDENT'),
(46, 'Oscar Parker', 'oscar.parker@student.edu', 'STUDENT'),
(47, 'Penny Collins', 'penny.collins@student.edu', 'STUDENT'),
(48, 'Quincy Edwards', 'quincy.edwards@student.edu', 'STUDENT'),
(49, 'Rose Stewart', 'rose.stewart@student.edu', 'STUDENT'),
(50, 'Sam Flores', 'sam.flores@student.edu', 'STUDENT'),
(51, 'Tara Morris', 'tara.morris@student.edu', 'STUDENT'),
(52, 'Ulysses Nguyen', 'ulysses.nguyen@student.edu', 'STUDENT'),
(53, 'Vera Murphy', 'vera.murphy@student.edu', 'STUDENT'),
(54, 'Will Rivera', 'will.rivera@student.edu', 'STUDENT'),
(55, 'Zoe Cook', 'zoe.cook@student.edu', 'STUDENT');

-- Inserting profiles for all users
INSERT INTO profiles (id, user_id, bio, avatar_url) VALUES
(1, 1, 'Experienced Computer Science Professor', 'https://example.com/avatars/john.jpg'),
(2, 2, 'Mathematics Expert with 10+ years teaching experience', 'https://example.com/avatars/emily.jpg'),
(3, 3, 'History teacher passionate about ancient civilizations', 'https://example.com/avatars/michael.jpg'),
(4, 4, 'Physics teacher with focus on mechanics', 'https://example.com/avatars/sarah.jpg'),
(5, 5, 'Literature professor with expertise in classic works', 'https://example.com/avatars/david.jpg'),
(6, 6, 'Computer Science student with interest in AI', 'https://example.com/avatars/alice.jpg'),
(7, 7, 'Mathematics major exploring algorithms', 'https://example.com/avatars/bob.jpg'),
(8, 8, 'History enthusiast with passion for ancient cultures', 'https://example.com/avatars/carol.jpg'),
(9, 9, 'Physics student interested in quantum mechanics', 'https://example.com/avatars/daniel.jpg'),
(10, 10, 'Literature student with focus on modern works', 'https://example.com/avatars/eva.jpg'),
(11, 11, 'Computer Science major', 'https://example.com/avatars/frank.jpg'),
(12, 12, 'Mathematics student', 'https://example.com/avatars/grace.jpg'),
(13, 13, 'History student', 'https://example.com/avatars/henry.jpg'),
(14, 14, 'Physics major', 'https://example.com/avatars/iris.jpg'),
(15, 15, 'English Literature student', 'https://example.com/avatars/jack.jpg'),
(16, 16, 'Computer Science student', 'https://example.com/avatars/kate.jpg'),
(17, 17, 'Mathematics enthusiast', 'https://example.com/avatars/liam.jpg'),
(18, 18, 'History major', 'https://example.com/avatars/mia.jpg'),
(19, 19, 'Physics student', 'https://example.com/avatars/nathan.jpg'),
(20, 20, 'Literature major', 'https://example.com/avatars/olivia.jpg'),
(21, 21, 'Computer Science student', 'https://example.com/avatars/paul.jpg'),
(22, 22, 'Mathematics student', 'https://example.com/avatars/quinn.jpg'),
(23, 23, 'History student', 'https://example.com/avatars/rachel.jpg'),
(24, 24, 'Physics major', 'https://example.com/avatars/steve.jpg'),
(25, 25, 'English student', 'https://example.com/avatars/tina.jpg'),
(26, 26, 'Computer Science major', 'https://example.com/avatars/uma.jpg'),
(27, 27, 'Mathematics major', 'https://example.com/avatars/victor.jpg'),
(28, 28, 'History enthusiast', 'https://example.com/avatars/wendy.jpg'),
(29, 29, 'Physics student', 'https://example.com/avatars/xavier.jpg'),
(30, 30, 'Literature major', 'https://example.com/avatars/yara.jpg'),
(31, 31, 'Computer Science student', 'https://example.com/avatars/zack.jpg'),
(32, 32, 'Mathematics student', 'https://example.com/avatars/ava.jpg'),
(33, 33, 'History student', 'https://example.com/avatars/ben.jpg'),
(34, 34, 'Physics major', 'https://example.com/avatars/cara.jpg'),
(35, 35, 'English Literature student', 'https://example.com/avatars/derek.jpg'),
(36, 36, 'Computer Science major', 'https://example.com/avatars/elena.jpg'),
(37, 37, 'Mathematics student', 'https://example.com/avatars/felix.jpg'),
(38, 38, 'History major', 'https://example.com/avatars/gina.jpg'),
(39, 39, 'Physics student', 'https://example.com/avatars/hugo.jpg'),
(40, 40, 'Literature student', 'https://example.com/avatars/ivy.jpg'),
(41, 41, 'Computer Science major', 'https://example.com/avatars/jake.jpg'),
(42, 42, 'Mathematics enthusiast', 'https://example.com/avatars/kara.jpg'),
(43, 43, 'History student', 'https://example.com/avatars/luna.jpg'),
(44, 44, 'Physics major', 'https://example.com/avatars/max.jpg'),
(45, 45, 'English student', 'https://example.com/avatars/nina.jpg'),
(46, 46, 'Computer Science student', 'https://example.com/avatars/oscar.jpg'),
(47, 47, 'Mathematics major', 'https://example.com/avatars/penny.jpg'),
(48, 48, 'History student', 'https://example.com/avatars/quincy.jpg'),
(49, 49, 'Physics student', 'https://example.com/avatars/rose.jpg'),
(50, 50, 'Literature major', 'https://example.com/avatars/sam.jpg'),
(51, 51, 'Computer Science student', 'https://example.com/avatars/tara.jpg'),
(52, 52, 'Mathematics student', 'https://example.com/avatars/ulysses.jpg'),
(53, 53, 'History major', 'https://example.com/avatars/vera.jpg'),
(54, 54, 'Physics student', 'https://example.com/avatars/will.jpg'),
(55, 55, 'English Literature student', 'https://example.com/avatars/zoe.jpg');

-- Inserting courses associated with teachers
INSERT INTO courses (id, title, description, teacher_id, duration) VALUES
(1, 'Introduction to Computer Science', 'Basic concepts of programming and algorithms', 1, 30),
(2, 'Advanced Mathematics', 'Calculus, linear algebra and statistics', 2, 45),
(3, 'World History', 'Survey of world civilizations from ancient to modern', 3, 60),
(4, 'General Physics', 'Fundamentals of mechanics, thermodynamics and waves', 4, 40),
(5, 'English Literature', 'Major works from English and American authors', 5, 35),
(6, 'Chemistry Fundamentals', 'Basics of chemistry including atomic structure and reactions', 1, 30),
(7, 'Biology Essentials', 'Cell biology, genetics and evolution', 2, 45),
(8, 'Introduction to Psychology', 'Basic psychological concepts and theories', 3, 40),
(9, 'Economics Principles', 'Micro and macroeconomics fundamentals', 4, 35),
(10, 'Art History', 'Major movements and artists from Renaissance to present', 5, 50);

-- Inserting modules for each course (3 modules per course)
INSERT INTO modules (id, course_id, title, order_index, description) VALUES
-- Course 1 modules
(1, 1, 'Programming Fundamentals', 1, 'Basic programming concepts and syntax'),
(2, 1, 'Data Structures', 2, 'Arrays, lists, and basic data organization'),
(3, 1, 'Algorithms', 3, 'Introduction to algorithm design and analysis'),
-- Course 2 modules
(4, 2, 'Calculus Basics', 1, 'Limits, derivatives and basic integration'),
(5, 2, 'Linear Algebra', 2, 'Vectors, matrices and linear transformations'),
(6, 2, 'Probability', 3, 'Probability theory and statistical analysis'),
-- Course 3 modules
(7, 3, 'Ancient Civilizations', 1, 'Egypt, Greece and Rome'),
(8, 3, 'Medieval Period', 2, 'Middle Ages and Renaissance'),
(9, 3, 'Modern Era', 3, 'Enlightenment to 20th century'),
-- Course 4 modules
(10, 4, 'Mechanics', 1, 'Motion, forces and energy'),
(11, 4, 'Thermodynamics', 2, 'Heat and temperature'),
(12, 4, 'Waves and Optics', 3, 'Wave motion and light'),
-- Course 5 modules
(13, 5, 'Classical Literature', 1, 'Shakespeare and earlier works'),
(14, 5, 'Romantic Period', 2, '18th and 19th century literature'),
(15, 5, 'Modern Literature', 3, '20th and 21st century works'),
-- Course 6 modules
(16, 6, 'Atomic Structure', 1, 'Atoms, elements and periodic table'),
(17, 6, 'Chemical Bonds', 2, 'Ionic, covalent and metallic bonds'),
(18, 6, 'Chemical Reactions', 3, 'Types of reactions and equations'),
-- Course 7 modules
(19, 7, 'Cell Biology', 1, 'Structure and function of cells'),
(20, 7, 'Genetics', 2, 'DNA, genes and heredity'),
(21, 7, 'Evolution', 3, 'Natural selection and evolutionary processes'),
-- Course 8 modules
(22, 8, 'Psychology History', 1, 'Development of psychological thought'),
(23, 8, 'Cognitive Psychology', 2, 'Thinking, memory and perception'),
(24, 8, 'Social Psychology', 3, 'Behavior in social contexts'),
-- Course 9 modules
(25, 9, 'Microeconomics', 1, 'Individual markets and decision making'),
(26, 9, 'Macroeconomics', 2, 'National economies and policy'),
(27, 9, 'International Trade', 3, 'Global economic interactions'),
-- Course 10 modules
(28, 10, 'Renaissance Art', 1, 'Art from 14th to 17th centuries'),
(29, 10, 'Impressionism', 2, '19th century art movement'),
(30, 10, 'Modern Art', 3, '20th century artistic innovations');

-- Inserting lessons for each module (3 lessons per module)
INSERT INTO lessons (id, module_id, title, content, video_url) VALUES
-- Module 1 lessons
(1, 1, 'Variables and Data Types', 'Understanding variables, integers, strings, and booleans', 'https://example.com/videos/variables.mp4'),
(2, 1, 'Control Structures', 'If statements, loops, and conditional logic', 'https://example.com/videos/control.mp4'),
(3, 1, 'Functions', 'Defining and calling functions', 'https://example.com/videos/functions.mp4'),
-- Module 2 lessons
(4, 2, 'Arrays and Lists', 'Creating and manipulating arrays', 'https://example.com/videos/arrays.mp4'),
(5, 2, 'Linked Lists', 'Understanding linked list data structure', 'https://example.com/videos/linkedlists.mp4'),
(6, 2, 'Hash Tables', 'Storing data in key-value pairs', 'https://example.com/videos/hashtables.mp4'),
-- Module 3 lessons
(7, 3, 'Sorting Algorithms', 'Bubble sort, selection sort, and insertion sort', 'https://example.com/videos/sorting.mp4'),
(8, 3, 'Searching Algorithms', 'Binary search and linear search', 'https://example.com/videos/searching.mp4'),
(9, 3, 'Recursion', 'Understanding recursive functions', 'https://example.com/videos/recursion.mp4'),
-- Module 4 lessons
(10, 4, 'Limits', 'Concept of limits in calculus', 'https://example.com/videos/limits.mp4'),
(11, 4, 'Derivatives', 'Definition and computation of derivatives', 'https://example.com/videos/derivatives.mp4'),
(12, 4, 'Integration', 'Basic integration techniques', 'https://example.com/videos/integration.mp4'),
-- Module 5 lessons
(13, 5, 'Vectors', 'Vector operations and properties', 'https://example.com/videos/vectors.mp4'),
(14, 5, 'Matrices', 'Matrix operations and transformations', 'https://example.com/videos/matrices.mp4'),
(15, 5, 'Systems of Equations', 'Solving linear systems', 'https://example.com/videos/systems.mp4'),
-- Module 6 lessons
(16, 6, 'Probability Basics', 'Events, outcomes, and probability rules', 'https://example.com/videos/probability.mp4'),
(17, 6, 'Random Variables', 'Discrete and continuous random variables', 'https://example.com/videos/randomvars.mp4'),
(18, 6, 'Statistical Distributions', 'Normal, binomial and other distributions', 'https://example.com/videos/distributions.mp4'),
-- Module 7 lessons
(19, 7, 'Ancient Egypt', 'Pyramids, pharaohs, and hieroglyphs', 'https://example.com/videos/egypt.mp4'),
(20, 7, 'Ancient Greece', 'Democracy, philosophy, and mythology', 'https://example.com/videos/greece.mp4'),
(21, 7, 'Ancient Rome', 'Empire expansion and legacy', 'https://example.com/videos/rome.mp4'),
-- Module 8 lessons
(22, 8, 'Medieval Europe', 'Feudalism and manor life', 'https://example.com/videos/medieval.mp4'),
(23, 8, 'Renaissance', 'Revival of learning and arts', 'https://example.com/videos/renaissance.mp4'),
(24, 8, 'Age of Exploration', 'European expansion and colonization', 'https://example.com/videos/exploration.mp4'),
-- Module 9 lessons
(25, 9, 'Industrial Revolution', 'Changes in manufacturing and society', 'https://example.com/videos/industrial.mp4'),
(26, 9, 'World Wars', 'Global conflicts of the 20th century', 'https://example.com/videos/wars.mp4'),
(27, 9, 'Cold War', 'Tensions between superpowers', 'https://example.com/videos/coldwar.mp4'),
-- Module 10 lessons
(28, 10, 'Newton''s Laws', 'Fundamental principles of motion', 'https://example.com/videos/newton.mp4'),
(29, 10, 'Energy and Work', 'Conservation of energy', 'https://example.com/videos/energy.mp4'),
(30, 10, 'Momentum', 'Linear and angular momentum', 'https://example.com/videos/momentum.mp4'),
-- Module 11 lessons
(31, 11, 'Temperature and Heat', 'Thermal energy and heat transfer', 'https://example.com/videos/temperature.mp4'),
(32, 11, 'Laws of Thermodynamics', 'Energy conservation and entropy', 'https://example.com/videos/thermodynamics.mp4'),
(33, 11, 'Ideal Gas Law', 'Relationship between pressure, volume and temperature', 'https://example.com/videos/idealgas.mp4'),
-- Module 12 lessons
(34, 12, 'Wave Properties', 'Amplitude, frequency, and wavelength', 'https://example.com/videos/waveprops.mp4'),
(35, 12, 'Light and Reflection', 'Properties of light and reflection laws', 'https://example.com/videos/reflection.mp4'),
(36, 12, 'Refraction', 'Bending of light rays', 'https://example.com/videos/refraction.mp4'),
-- Module 13 lessons
(37, 13, 'Shakespeare''s Plays', 'Analysis of major tragedies and comedies', 'https://example.com/videos/shakespeare.mp4'),
(38, 13, 'Poetry Forms', 'Sonnet, epic, and other forms', 'https://example.com/videos/poetry.mp4'),
(39, 13, 'Literary Devices', 'Metaphor, symbolism, and imagery', 'https://example.com/videos/devices.mp4'),
-- Module 14 lessons
(40, 14, 'Romantic Poetry', 'Works by Wordsworth, Coleridge and Keats', 'https://example.com/videos/romanticpoetry.mp4'),
(41, 14, 'Novel Development', 'Emergence of the novel in 18th and 19th centuries', 'https://example.com/videos/novels.mp4'),
(42, 14, 'American Romanticism', 'Emerson, Thoreau, and Poe', 'https://example.com/videos/amromantic.mp4'),
-- Module 15 lessons
(43, 15, 'Modernist Literature', 'Woolf, Joyce, and stream of consciousness', 'https://example.com/videos/modernlit.mp4'),
(44, 15, 'Contemporary Fiction', 'Contemporary authors and themes', 'https://example.com/videos/contemporary.mp4'),
(45, 15, 'Post-colonial Literature', 'Voices from former colonies', 'https://example.com/videos/postcolonial.mp4'),
-- Module 16 lessons
(46, 16, 'Atomic Structure', 'Protons, neutrons, and electrons', 'https://example.com/videos/atomicstruct.mp4'),
(47, 16, 'Periodic Table', 'Organization and properties of elements', 'https://example.com/videos/periodictable.mp4'),
(48, 16, 'Isotopes', 'Variations in atomic structure', 'https://example.com/videos/isotopes.mp4'),
-- Module 17 lessons
(49, 17, 'Ionic Bonds', 'Transfer of electrons in ionic compounds', 'https://example.com/videos/ionic.mp4'),
(50, 17, 'Covalent Bonds', 'Sharing of electrons', 'https://example.com/videos/covalent.mp4'),
(51, 17, 'Metallic Bonds', 'Structure of metals', 'https://example.com/videos/metallic.mp4'),
-- Module 18 lessons
(52, 18, 'Chemical Reactions', 'Types and characteristics', 'https://example.com/videos/reactions.mp4'),
(53, 18, 'Balancing Equations', 'Stoichiometry and mole ratios', 'https://example.com/videos/balancing.mp4'),
(54, 18, 'Acids and Bases', 'pH and acid-base reactions', 'https://example.com/videos/acidsbases.mp4'),
-- Module 19 lessons
(55, 19, 'Cell Structure', 'Organelles and their functions', 'https://example.com/videos/cellstruct.mp4'),
(56, 19, 'Cell Membrane', 'Transport across membranes', 'https://example.com/videos/cellmembrane.mp4'),
(57, 19, 'Cellular Respiration', 'Energy production in cells', 'https://example.com/videos/respiration.mp4'),
-- Module 20 lessons
(58, 20, 'DNA Structure', 'Double helix and nucleotides', 'https://example.com/videos/dnastruct.mp4'),
(59, 20, 'Gene Expression', 'Transcription and translation', 'https://example.com/videos/geneexpr.mp4'),
(60, 20, 'Inheritance Patterns', 'Mendelian genetics', 'https://example.com/videos/inherpat.mp4'),
-- Module 21 lessons
(61, 21, 'Natural Selection', 'Darwin''s theory of evolution', 'https://example.com/videos/naturalselect.mp4'),
(62, 21, 'Speciation', 'Formation of new species', 'https://example.com/videos/speciation.mp4'),
(63, 21, 'Evidence of Evolution', 'Fossil record and comparative anatomy', 'https://example.com/videos/evidence.mp4'),
-- Module 22 lessons
(64, 22, 'Structuralism and Functionalism', 'Early schools of psychology', 'https://example.com/videos/earlypsych.mp4'),
(65, 22, 'Behaviorism', 'Watson, Skinner and conditioning', 'https://example.com/videos/behaviorism.mp4'),
(66, 22, 'Cognitive Revolution', 'Mental processes in psychology', 'https://example.com/videos/cognitive.mp4'),
-- Module 23 lessons
(67, 23, 'Memory Systems', 'Sensory, short-term, and long-term memory', 'https://example.com/videos/memory.mp4'),
(68, 23, 'Perception', 'Interpreting sensory information', 'https://example.com/videos/perception.mp4'),
(69, 23, 'Learning', 'Classical and operant conditioning', 'https://example.com/videos/learning.mp4'),
-- Module 24 lessons
(70, 24, 'Group Dynamics', 'Behavior in groups', 'https://example.com/videos/groupdynamics.mp4'),
(71, 24, 'Social Cognition', 'How we think about others', 'https://example.com/videos/socialcog.mp4'),
(72, 24, 'Prejudice and Discrimination', 'Social biases and their effects', 'https://example.com/videos/prejudice.mp4'),
-- Module 25 lessons
(73, 25, 'Supply and Demand', 'Market equilibrium', 'https://example.com/videos/supplydemand.mp4'),
(74, 25, 'Elasticity', 'Price sensitivity of demand and supply', 'https://example.com/videos/elasticity.mp4'),
(75, 25, 'Consumer Choice', 'Rational decision making', 'https://example.com/videos/consumer.mp4'),
-- Module 26 lessons
(76, 26, 'GDP and Growth', 'Measuring national output', 'https://example.com/videos/gdp.mp4'),
(77, 26, 'Unemployment', 'Types and measurement of unemployment', 'https://example.com/videos/unemployment.mp4'),
(78, 26, 'Inflation', 'Price level changes', 'https://example.com/videos/inflation.mp4'),
-- Module 27 lessons
(79, 27, 'Trade Theories', 'Comparative advantage', 'https://example.com/videos/tradetheory.mp4'),
(80, 27, 'Exchange Rates', 'Currency conversion and markets', 'https://example.com/videos/exchangerates.mp4'),
(81, 27, 'Globalization', 'World economic integration', 'https://example.com/videos/globalization.mp4'),
-- Module 28 lessons
(82, 28, 'Renaissance Masters', 'Leonardo, Michelangelo, and Raphael', 'https://example.com/videos/renaissancemasters.mp4'),
(83, 28, 'Architecture', 'Churches, palaces, and urban design', 'https://example.com/videos/architecture.mp4'),
(84, 28, 'Patronage System', 'Art sponsorship in Renaissance', 'https://example.com/videos/patronage.mp4'),
-- Module 29 lessons
(85, 29, 'Monet and Renoir', 'Impressionist painting techniques', 'https://example.com/videos/monet.mp4'),
(86, 29, 'Color Theory', 'Use of color in impressionist works', 'https://example.com/videos/colortheory.mp4'),
(87, 29, 'Plein Air Painting', 'Outdoor painting movement', 'https://example.com/videos/pleinair.mp4'),
-- Module 30 lessons
(88, 30, 'Abstract Expressionism', 'Pollock, Rothko and color field painting', 'https://example.com/videos/abstract.mp4'),
(89, 30, 'Pop Art', 'Warhol, Lichtenstein and popular culture', 'https://example.com/videos/popart.mp4'),
(90, 30, 'Contemporary Art', 'Conceptual and installation art', 'https://example.com/videos/contemporaryart.mp4');

-- Enrolling students in courses (random distribution)
INSERT INTO enrollments (user_id, course_id, enroll_date, status) VALUES
-- Students 6-15 in Course 1
(6, 1, '2024-09-01', 'Active'),
(7, 1, '2024-09-01', 'Active'),
(8, 1, '2024-09-01', 'Active'),
(9, 1, '2024-09-01', 'Active'),
(10, 1, '2024-09-01', 'Active'),
(11, 1, '2024-09-01', 'Active'),
(12, 1, '2024-09-01', 'Active'),
(13, 1, '2024-09-01', 'Active'),
(14, 1, '2024-09-01', 'Active'),
(15, 1, '2024-09-01', 'Active'),
-- Students 6-15 in Course 2
(6, 2, '2024-09-01', 'Active'),
(7, 2, '2024-09-01', 'Active'),
(8, 2, '2024-09-01', 'Active'),
(9, 2, '2024-09-01', 'Active'),
(10, 2, '2024-09-01', 'Active'),
(11, 2, '2024-09-01', 'Active'),
(12, 2, '2024-09-01', 'Active'),
(13, 2, '2024-09-01', 'Active'),
(14, 2, '2024-09-01', 'Active'),
(15, 2, '2024-09-01', 'Active'),
-- Students 16-25 in Course 3
(16, 3, '2024-09-01', 'Active'),
(17, 3, '2024-09-01', 'Active'),
(18, 3, '2024-09-01', 'Active'),
(19, 3, '2024-09-01', 'Active'),
(20, 3, '2024-09-01', 'Active'),
(21, 3, '2024-09-01', 'Active'),
(22, 3, '2024-09-01', 'Active'),
(23, 3, '2024-09-01', 'Active'),
(24, 3, '2024-09-01', 'Active'),
(25, 3, '2024-09-01', 'Active'),
-- Students 16-25 in Course 4
(16, 4, '2024-09-01', 'Active'),
(17, 4, '2024-09-01', 'Active'),
(18, 4, '2024-09-01', 'Active'),
(19, 4, '2024-09-01', 'Active'),
(20, 4, '2024-09-01', 'Active'),
(21, 4, '2024-09-01', 'Active'),
(22, 4, '2024-09-01', 'Active'),
(23, 4, '2024-09-01', 'Active'),
(24, 4, '2024-09-01', 'Active'),
(25, 4, '2024-09-01', 'Active'),
-- Students 26-35 in Course 5
(26, 5, '2024-09-01', 'Active'),
(27, 5, '2024-09-01', 'Active'),
(28, 5, '2024-09-01', 'Active'),
(29, 5, '2024-09-01', 'Active'),
(30, 5, '2024-09-01', 'Active'),
(31, 5, '2024-09-01', 'Active'),
(32, 5, '2024-09-01', 'Active'),
(33, 5, '2024-09-01', 'Active'),
(34, 5, '2024-09-01', 'Active'),
(35, 5, '2024-09-01', 'Active'),
-- Students 26-35 in Course 6
(26, 6, '2024-09-01', 'Active'),
(27, 6, '2024-09-01', 'Active'),
(28, 6, '2024-09-01', 'Active'),
(29, 6, '2024-09-01', 'Active'),
(30, 6, '2024-09-01', 'Active'),
(31, 6, '2024-09-01', 'Active'),
(32, 6, '2024-09-01', 'Active'),
(33, 6, '2024-09-01', 'Active'),
(34, 6, '2024-09-01', 'Active'),
(35, 6, '2024-09-01', 'Active'),
-- Students 36-45 in Course 7
(36, 7, '2024-09-01', 'Active'),
(37, 7, '2024-09-01', 'Active'),
(38, 7, '2024-09-01', 'Active'),
(39, 7, '2024-09-01', 'Active'),
(40, 7, '2024-09-01', 'Active'),
(41, 7, '2024-09-01', 'Active'),
(42, 7, '2024-09-01', 'Active'),
(43, 7, '2024-09-01', 'Active'),
(44, 7, '2024-09-01', 'Active'),
(45, 7, '2024-09-01', 'Active'),
-- Students 36-45 in Course 8
(36, 8, '2024-09-01', 'Active'),
(37, 8, '2024-09-01', 'Active'),
(38, 8, '2024-09-01', 'Active'),
(39, 8, '2024-09-01', 'Active'),
(40, 8, '2024-09-01', 'Active'),
(41, 8, '2024-09-01', 'Active'),
(42, 8, '2024-09-01', 'Active'),
(43, 8, '2024-09-01', 'Active'),
(44, 8, '2024-09-01', 'Active'),
(45, 8, '2024-09-01', 'Active'),
-- Students 46-55 in Course 9
(46, 9, '2024-09-01', 'Active'),
(47, 9, '2024-09-01', 'Active'),
(48, 9, '2024-09-01', 'Active'),
(49, 9, '2024-09-01', 'Active'),
(50, 9, '2024-09-01', 'Active'),
(51, 9, '2024-09-01', 'Active'),
(52, 9, '2024-09-01', 'Active'),
(53, 9, '2024-09-01', 'Active'),
(54, 9, '2024-09-01', 'Active'),
(55, 9, '2024-09-01', 'Active'),
-- Students 46-55 in Course 10
(46, 10, '2024-09-01', 'Active'),
(47, 10, '2024-09-01', 'Active'),
(48, 10, '2024-09-01', 'Active'),
(49, 10, '2024-09-01', 'Active'),
(50, 10, '2024-09-01', 'Active'),
(51, 10, '2024-09-01', 'Active'),
(52, 10, '2024-09-01', 'Active'),
(53, 10, '2024-09-01', 'Active'),
(54, 10, '2024-09-01', 'Active'),
(55, 10, '2024-09-01', 'Active');

-- Adding course reviews from students
INSERT INTO course_reviews (id, course_id, student_id, rating, comment, created_at) VALUES
-- Reviews for Course 1 (Introduction to Computer Science)
(1, 1, 6, 5, 'Excellent introduction to programming concepts. The instructor explains everything clearly.', '2024-10-15 10:30:00'),
(2, 1, 8, 4, 'Good course content but assignments could be more challenging.', '2024-10-18 14:20:00'),
(3, 1, 12, 5, 'Perfect for beginners. I learned so much in just a few weeks!', '2024-10-20 09:15:00'),
(4, 1, 14, 4, 'Well structured course with practical examples.', '2024-10-22 16:45:00'),
(5, 1, 10, 5, 'The modules are organized logically and build on each other well.', '2024-10-25 11:30:00'),

-- Reviews for Course 2 (Advanced Mathematics)
(6, 2, 7, 4, 'Comprehensive coverage of calculus and linear algebra concepts.', '2024-10-16 13:20:00'),
(7, 2, 9, 5, 'Challenging but rewarding. The teacher has excellent mathematical knowledge.', '2024-10-19 15:45:00'),
(8, 2, 11, 3, 'Good content but pace was a bit too fast for me.', '2024-10-21 12:10:00'),
(9, 2, 13, 4, 'Perfect for students who want to strengthen their math foundation.', '2024-10-23 10:25:00'),
(10, 2, 15, 5, 'Brilliant explanations and practical applications.', '2024-10-26 14:00:00'),

-- Reviews for Course 5 (English Literature)
(11, 5, 26, 5, 'Fascinating journey through literary history. Loved the analysis of classic works.', '2024-10-17 09:45:00'),
(12, 5, 28, 4, 'Good course but would like more modern literature included.', '2024-10-20 11:20:00'),
(13, 5, 30, 5, 'The instructor brings literature to life with engaging discussions.', '2024-10-22 13:30:00'),
(14, 5, 32, 4, 'Well organized modules with excellent reading selections.', '2024-10-24 15:15:00'),
(15, 5, 34, 5, 'Perfect blend of classical and contemporary approaches to literature.', '2024-10-27 12:00:00'),

-- Reviews for Course 8 (Introduction to Psychology)
(16, 8, 36, 4, 'Interesting overview of psychological concepts and theories.', '2024-10-18 10:10:00'),
(17, 8, 38, 5, 'Excellent course that sparked my interest in pursuing psychology further.', '2024-10-21 14:30:00'),
(18, 8, 40, 4, 'Good balance of historical context and modern research.', '2024-10-23 16:00:00'),
(19, 8, 42, 3, 'Interesting subject but lectures felt a bit dry.', '2024-10-25 11:45:00'),
(20, 8, 44, 5, 'The cognitive psychology module was particularly insightful.', '2024-10-28 13:10:00'),

-- Reviews for Course 10 (Art History)
(21, 10, 46, 5, 'Incredible journey through art history. Beautiful visual examples.', '2024-10-19 12:20:00'),
(22, 10, 48, 4, 'Learned so much about art movements I never knew existed.', '2024-10-22 10:05:00'),
(23, 10, 50, 5, 'The instructor has deep knowledge and passion for the subject.', '2024-10-24 15:20:00'),
(24, 10, 52, 4, 'Great organization from Renaissance to contemporary art.', '2024-10-26 09:30:00'),
(25, 10, 54, 5, 'Perfect course for art enthusiasts and beginners alike.', '2024-10-29 14:40:00');