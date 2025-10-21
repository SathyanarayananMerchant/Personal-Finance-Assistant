-- Display student name and the department who are enrolled in computer science
SELECT S.stu_name, D.dept_name FROM Students S JOIN Departments D on S.dept_id = D.dept_id WHERE D.dept_name = 'Computer Science';

-- Prof teaching more than one course
SELECT p.prof_id, p.name, COUNT() AS course_count FROM Professors p JOIN Courses c ON p.prof_id = c.prof_id GROUP BY p.prof_id, p.name HAVING COUNT() > 1;

-- List all students enrolled in Database course
SELECT s.stu_id, s.stu_name FROM Students s JOIN Enrollment e ON s.stu_id = e.stu_id JOIN Courses c ON e.course_id = c.course_id WHERE c.course_name = 'Database Systems';

-- (USING SET) NAME ID OF STUDENT ALONG WITH COURSE NAME WHO ARE ENROLLED IN ANY TWO COURSES OF Database and operating system
SELECT s.stu_id, s.stu_name, 'Database Systems & Operating Systems' AS enrolled_in FROM Students s
JOIN (
    SELECT stu_id
    FROM Enrollment e
    JOIN Courses c ON e.course_id = c.course_id
    WHERE c.course_name = 'Database Systems'
) db ON s.stu_id = db.stu_id
JOIN (
    SELECT stu_id
    FROM Enrollment e
    JOIN Courses c ON e.course_id = c.course_id
    WHERE c.course_name = 'Operating Systems'
) os ON s.stu_id = os.stu_id;


-- (USING SET) NAME ID OF STUDENT ALONG WITH COURSE NAME WHO ARE ENROLLED IN ANY TWO COURSES OF Database or operating system
SELECT S.stu_id, S.stu_name FROM Students S
JOIN Enrollment E on S.stu_id = E.stu_id
JOIN Courses C on E.course_id = C.course_id
WHERE C.course_name = 'Database Systems'
UNION
SELECT S.stu_id, S.stu_name
FROM Students S
JOIN Enrollment E on S.stu_id = E.stu_id
JOIN Courses C on E.course_id = C.course_id
WHERE C.course_name = 'Operating Systems';

-- Enrolled in database but not in operating system student name (USING set Operating System)
(
    SELECT S.stu_id, S.stu_name
    FROM Students S
    JOIN Enrollment E on S.stu_id = E.stu_id
    JOIN Courses C on E.course_id = C.course_id
    WHERE C.course_name = 'Database Systems'
) EXCEPT (
    SELECT S.stu_id, S.stu_name
    FROM Students S
    JOIN Enrollment E on S.stu_id = E.stu_id
    JOIN Courses C on E.course_id = C.course_id
    WHERE C.course_name = 'Operating Systems'
);

-- LIST DEPARTMENTS WITH NO PROF.
SELECT D.dept_id, D.dept_name
FROM Departments D
LEFT JOIN Professors P on D.dept_id = P.dept_id
WHERE P.prof_id IS NULL;


