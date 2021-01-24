INSERT INTO action (id, name) VALUES
    (1, "add_project"),
    (2, "add_travel_order"),
    (3, "approve_travel_order_clerk"),
    (4, "approve_travel_order_supervisor"),
    (5, "add_expense"),
    (6, "approve_expense"),
    (7, "view_reports");

INSERT INTO role (id, name) VALUES
    (1, "user"),
    (2, "clerk"),
    (3, "supervisor");

INSERT INTO role_action_list (role_id, action_list_id) VALUES
    (1, 2),
    (1, 5),
    (2, 3),
    (2, 6),
    (2, 7),
    (3, 1),
    (3, 2),
    (3, 4),
    (3, 5),
    (3, 7);

INSERT INTO user (id, password, username, role_id) VALUES
    (null, "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08", "Marius Supuran", 3),
    (null, "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08", "Lucian Dragan", 2),
    (null, "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08", "Valer Cosara", 1);