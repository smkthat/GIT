delete from todo_task;

/*
 * create_date time in UTC local zone
 * update_date time in UTC local zone
*/

insert into todo_task(id, title, description, create_date, update_date, complete) values
(
    1,
    'Test 1',
    'Test todo task with id=1',
    '2020-01-01 01:00:00.0',
    '2020-01-01 01:00:00.0',
    false);

insert into todo_task(id, title, description, create_date, update_date, complete) values
(
    2,
    'Test 2',
    'Test todo task with id=2',
    '2020-01-01 02:00:00.0',
    '2020-01-01 02:00:00.0',
    false);

insert into todo_task(id, title, description, create_date, update_date, complete) values
(
    3,
    'Test 3',
    'Test todo task with id=3',
    '2020-01-01 03:00:00.0',
    '2020-01-01 04:00:00.0',
    true);

update hibernate_sequence set next_val=10;