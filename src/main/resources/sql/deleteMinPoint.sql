CREATE OR REPLACE FUNCTION delete_object_by_minimal_point(target_point FLOAT, owner BIGINT)
    RETURNS VOID AS $$
BEGIN
    DELETE FROM lab_work
    WHERE lab_work_id  = (
        SELECT lab_work_id
        FROM lab_work
        WHERE minimal_point = target_point AND created_by = owner
        LIMIT 1
    );
END;
$$ LANGUAGE plpgsql;
