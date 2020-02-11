CREATE TABLE linked_purchase_list (
    student_id INT(11) UNSIGNED NOT NULL,
    course_id INT(11) UNSIGNED NOT NULL,
    price INT(11),
    subscription_date DATETIME(6),
    primary key (
        course_id,
        student_id
    )
);