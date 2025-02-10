$(document).ready(function () {
    loadUsers();
    loadUserData();

        // Переключение между User и Admin
        document.getElementById("adminTabBtn").addEventListener("click", function () {
            document.getElementById("admin-section").classList.remove("d-none");
            document.getElementById("admin-section").classList.add("active");
            document.getElementById("user-section").classList.remove("active")
            document.getElementById("user-section").classList.add("d-none");
        });

        document.getElementById("userTabBtn").addEventListener("click", function () {
            document.getElementById("user-section").classList.remove("d-none");
            document.getElementById("user-section").classList.add("active");
            document.getElementById("admin-section").classList.remove("active")
            document.getElementById("admin-section").classList.add("d-none");
        });

        // Переключение между Users Table и New User в админке
        document.getElementById("usersTableTabBtn").addEventListener("click", function () {
            document.getElementById("usersTableSection").classList.remove("d-none");
            document.getElementById("usersTableSection").classList.add("active")
            document.getElementById("newUserSection").classList.remove("active");
            document.getElementById("newUserSection").classList.add("d-none")
        });

        document.getElementById("newUserTabBtn").addEventListener("click", function () {
            document.getElementById("newUserSection").classList.remove("d-none");
            document.getElementById("newUserSection").classList.add("active")
            document.getElementById("usersTableSection").classList.remove("active");
            document.getElementById("usersTableSection").classList.add("d-none")
        });

        // Загружаем данные пользователя при загрузке страницы
        document.addEventListener("DOMContentLoaded", loadUserData);



    // User info top left on page
    function loadUserData() {
        fetch("/api/get_user")
            .then(response => response.json())
            .then(data => {
                document.getElementById("userEmail").textContent = data.user.email;
                document.getElementById("shortRoles").textContent = data.shortRoles.join(", ");

                if (data.shortRoles.includes("ADMIN")) {
                    document.getElementById("adminTabBtn").classList.remove("d-none");
                }
            });
    }

    // Загрузка пользователей
    function loadUsers() {
        fetch('/api/all_users')
            .then(response => response.json())
            .then(data => {
                const tbody = $('#usersTableBody');
                tbody.empty(); // Очищаем таблицу перед загрузкой новых данных

                data.forEach(user => {
                    tbody.append(`
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.firstName}</td>
                            <td>${user.secondName}</td>
                            <td>${user.age}</td>
                            <td>${user.email}</td>
                            <td>${user.shortStringRoles}</td>
                            <td>
                                <button class="btn btn-primary editBtn" data-id="${user.id}" data-password="${user.password}">Edit</button>
                            </td>
                            <td>
                                <button class="btn btn-danger deleteBtn" data-id="${user.id}">Delete</button>
                            </td>
                        </tr>
                    `);
                });

                // buttons Edit & Delete
                $('.editBtn').click(handleEdit);
                $('.deleteBtn').click(handleDelete);
            });
    }

    // Add User
    $('#newUserForm').submit(function (e) {
        e.preventDefault();

        fetch('/api/nextId')
            .then(response => response.json())
            .then(nextId => {
                const user = {
                    id: nextId,
                    firstName: $('#newFirstName').val(),
                    secondName: $('#newSecondName').val(),
                    age: $('#newAge').val(),
                    email: $('#newEmail').val(),
                    password: $('#newPassword').val(),
                    selectedRole: $('#newSelectedRole').val()
                };

                fetch(`/api/add?selectedRole=${user.selectedRole}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(user)
                })
                    .then(response => {
                        if (response.status === 201) {
                            $('#newUserForm')[0].reset();
                            loadUsers();
                            // Переключаем активную вкладку на usersTable
                            $('#newUser-tab').removeClass('active show'); // Деактивируем текущую вкладку
                            $('#usersTable-tab').addClass('active show'); // Делаем активной вкладку usersTable
                            $('#newUserFormContainer').removeClass('show active'); // Прячем форму
                            $('#usersTable').addClass('show active'); // Показываем таблицу пользователей
                        } else if (response.status === 226) {
                            alert('User with this ID already exists')
                        } else {
                            alert('Failed to add new user')
                        }
                    })
                    .catch(error => console.error('Error adding user:', error));
            });
    })


    // Обработчик кнопки "Delete"
    // Извлекаем данные пользователя из строки таблицы, к которой привязана кнопка
    function handleDelete() {
        const row = $(this).closest('tr');
        const userId = row.find('td').eq(0).text().trim();
        const firstName = row.find('td').eq(1).text().trim();
        const secondName = row.find('td').eq(2).text().trim();
        const age = row.find('td').eq(3).text().trim();
        const email = row.find('td').eq(4).text().trim();
        const roles = row.find('td').eq(5).text().trim();

        // Заполняем поля модального окна
        $('#deleteUserId').val(userId);
        $('#deleteId').val(userId);
        $('#deleteFirstName').val(firstName);
        $('#deleteSecondName').val(secondName);
        $('#deleteAge').val(age);
        $('#deleteEmail').val(email);
        $('#deleteRoles').val(roles);

        // Показываем модальное окно
        $('#deleteModal').modal('show');
    }

    // Обработчик отправки формы удаления пользователя
    $('#deleteUserForm').submit(function (e) {
        e.preventDefault();

        const userId = $('#deleteUserId').val();

        fetch(`/api/delete?id=${userId}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (response.ok) {
                    $('#deleteModal').modal('hide');  // Закрываем модальное окно
                    loadUsers();  // Обновляем список пользователей после удаления
                } else {
                    alert('Failed to delete user');
                }
            })
            .catch(error => console.error('Error deleting user:', error));
    });

    // Обработка Edit user
    function handleEdit() {
        const row = $(this).closest('tr');
        const userId = row.find('td').eq(0).text().trim();
        const firstName = row.find('td').eq(1).text().trim();
        const secondName = row.find('td').eq(2).text().trim();
        const age = row.find('td').eq(3).text().trim();
        const email = row.find('td').eq(4).text().trim();
        const password = $(this).data('password');
        const roles = row.find('td').eq(5).text().trim();
        let selectedRole = null

        if (roles.includes("ADMIN")) {
            selectedRole = "ROLE_ADMIN"
        } else {
            selectedRole = "ROLE_USER"
        }

        $('#editUserId').val(userId);
        $('#editId').val(userId);
        $('#editFirstName').val(firstName);
        $('#editSecondName').val(secondName);
        $('#editAge').val(age);
        $('#editEmail').val(email);
        $('#editPassword').val(password);
        //$('#editRole').val(roles);
        $('#editSelectedRole').val(selectedRole)

        $('#editModal').modal('show');

    }

    // Обработчик отправки формы изменения пользователя
    $('#editUserForm').submit(function (e) {
        e.preventDefault();

        const user = {
            id: $('#editUserId').val(),
            firstName: $('#editFirstName').val(),
            secondName: $('#editSecondName').val(),
            age: $('#editAge').val(),
            email: $('#editEmail').val(),
            password: $('#editPassword').val(),
            //roles: $('#editRole').val()
            selectedRole: $('#editSelectedRole').val()
        };

        fetch(`/api/edit?id=${user.id}&selectedRole=${user.selectedRole}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user)
        })
            .then(response => {
                if (response.ok) {
                    $('#editModal').modal('hide');
                    loadUsers(); // Обновляем таблицу
                } else {
                    alert('Failed to edit user');
                }
            })
            .catch(error => console.error('Error editing user:', error));
    });
})

