insert into team(id, name, introduction)
values (1, 'Team_A', 'Good Team A'),
       (2, 'Team_B', 'Best Team B'),
       (3, 'Team_C', 'Great Team C');

insert into member(id, name, age, team_id)
values (1, 'Alice', 29, 1),
       (2, 'Benjamin', 25, 1),
       (3, 'Claire', 27, 1),
       (4, 'Daniel', 30, 2),
       (5, 'Emily', 31, 2),
       (6, 'Frances', 26, 2),
       (7, 'Grace', 29, 3),
       (8, 'Henry', 30, 3),
       (9, 'Isabel', 26, 3);


insert into team_eager(id, name, introduction)
values (1, 'Team_A', 'Good Team A'),
       (2, 'Team_B', 'Best Team B'),
       (3, 'Team_C', 'Great Team C');

insert into member_eager(id, name, age, team_eager_id)
values (1, 'Alice', 29, 1),
       (2, 'Benjamin', 25, 1),
       (3, 'Claire', 27, 1),
       (4, 'Daniel', 30, 2),
       (5, 'Emily', 31, 2),
       (6, 'Frances', 26, 2),
       (7, 'Grace', 29, 3),
       (8, 'Henry', 30, 3),
       (9, 'Isabel', 26, 3);


insert into team_sub(id, name, introduction)
values (1, 'Team_A', 'Good Team A'),
       (2, 'Team_B', 'Best Team B'),
       (3, 'Team_C', 'Great Team C');

insert into member_sub(id, name, age, team_sub_id)
values (1, 'Alice', 29, 1),
       (2, 'Benjamin', 25, 1),
       (3, 'Claire', 27, 1),
       (4, 'Daniel', 30, 2),
       (5, 'Emily', 31, 2),
       (6, 'Frances', 26, 2),
       (7, 'Grace', 29, 3),
       (8, 'Henry', 30, 3),
       (9, 'Isabel', 26, 3);