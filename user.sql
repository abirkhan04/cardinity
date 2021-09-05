INSERT INTO `users` (`id`, `active`, `name`, `password`, `username`) VALUES ('1', b'1', 'Evaldas Tylas', '$2a$10$CucC5fb6BNlP.CtIBYnPwOD04gI596W7j71noRaSndclaH0JDSIHS', 'evaldas');
INSERT INTO `roles` (`id`, `role`) VALUES ('1', 'ADMIN');
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES ('1', '1');

