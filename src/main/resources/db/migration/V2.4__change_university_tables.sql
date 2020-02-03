ALTER TABLE faculty ADD CONSTRAINT faculty_manager_uniq UNIQUE (manager_id);
ALTER TABLE department ADD CONSTRAINT department_manager_uniq UNIQUE (manager_id);
