<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home - Resource Manager</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link rel="stylesheet" href="css/style.css">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: Arial, sans-serif;
            background-color: #f5f5f5;
        }

        .navbar {
            background-color: #2c3e50;
            color: white;
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .navbar-left {
            display: flex;
            gap: 30px;
        }

        .nav-menu {
            list-style: none;
            display: flex;
            gap: 20px;
        }

        .nav-menu a {
            color: white;
            text-decoration: none;
            cursor: pointer;
            padding: 8px 15px;
            border-radius: 4px;
            transition: background-color 0.3s;
        }

        .nav-menu a:hover {
            background-color: #34495e;
        }

        .navbar-right {
            position: relative;
        }

        .user-profile {
            display: flex;
            align-items: center;
            gap: 10px;
            cursor: pointer;
        }

        .user-avatar {
            width: 35px;
            height: 35px;
            border-radius: 50%;
            background-color: #667eea;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
            font-size: 18px;
        }

        .dropdown-menu {
            position: absolute;
            top: 100%;
            right: 0;
            background-color: white;
            color: #333;
            min-width: 150px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            border-radius: 4px;
            display: none;
            margin-top: 10px;
            z-index: 1000;
        }

        .dropdown-menu.show {
            display: block;
        }

        .dropdown-menu a {
            display: block;
            padding: 12px 20px;
            color: #333;
            text-decoration: none;
            border-bottom: 1px solid #eee;
            transition: background-color 0.3s;
        }

        .dropdown-menu a:last-child {
            border-bottom: none;
        }

        .dropdown-menu a:hover {
            background-color: #f0f0f0;
        }

        .dropdown-menu a.logout {
            color: #e74c3c;
        }

        .container {
            display: flex;
            height: calc(100vh - 60px);
        }

        .sidebar {
            width: 250px;
            background-color: #34495e;
            color: white;
            padding: 20px;
            box-shadow: 2px 0 4px rgba(0, 0, 0, 0.1);
        }

        .sidebar h3 {
            margin-bottom: 20px;
            font-size: 18px;
        }

        .sidebar-menu {
            list-style: none;
        }

        .sidebar-menu li {
            margin-bottom: 10px;
        }

        .sidebar-menu a {
            color: white;
            text-decoration: none;
            display: block;
            padding: 10px 15px;
            border-radius: 4px;
            transition: background-color 0.3s;
            cursor: pointer;
        }

        .sidebar-menu a:hover,
        .sidebar-menu a.active {
            background-color: #2c3e50;
        }

        .content {
            flex: 1;
            padding: 30px;
            overflow-y: auto;
        }

        .section {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;
        }

        .section h2 {
            color: #2c3e50;
            margin-bottom: 20px;
            border-bottom: 2px solid #667eea;
            padding-bottom: 10px;
        }

        .resource-item {
            background-color: #f9f9f9;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 10px;
            border-left: 4px solid #667eea;
        }

        .resource-item strong {
            color: #2c3e50;
        }

        .loading {
            text-align: center;
            color: #999;
            padding: 20px;
        }

        .spinner {
            border: 3px solid #f3f3f3;
            border-top: 3px solid #667eea;
            border-radius: 50%;
            width: 30px;
            height: 30px;
            animation: spin 1s linear infinite;
            margin: 0 auto;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }

        .success-message {
            background-color: #d4edda;
            color: #155724;
            padding: 12px;
            border-radius: 4px;
            margin-bottom: 15px;
            border: 1px solid #c3e6cb;
        }

        .error-message {
            background-color: #f8d7da;
            color: #721c24;
            padding: 12px;
            border-radius: 4px;
            margin-bottom: 15px;
            border: 1px solid #f5c6cb;
        }

        .resource-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 20px;
        }

        .resource-card {
            background-color: white;
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s, box-shadow 0.3s;
        }

        .resource-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
        }

        .resource-card h3 {
            color: #2c3e50;
            margin-bottom: 10px;
        }

        .resource-card p {
            color: #666;
            margin-bottom: 5px;
        }
    </style>
</head>
<body>
    <!-- Navigation Bar -->
    <div class="navbar">
        <div class="navbar-left">
            <h2 style="margin: 0;">Resource Manager</h2>
            <ul class="nav-menu">
                <li><a onclick="loadDashboard()">Dashboard</a></li>
                <li><a onclick="loadResources()">Resources</a></li>
                <li><a onclick="loadSettings()">Settings</a></li>
            </ul>
        </div>
        <div class="navbar-right">
            <div class="user-profile" onclick="toggleDropdown()">
                <div class="user-avatar"><%= username.charAt(0).toUpperCase() %></div>
                <span><%= username %></span>
                <div class="dropdown-menu" id="dropdownMenu">
                    <a onclick="loadProfile()">Profile</a>
                    <a class="logout" onclick="logout()">Logout</a>
                </div>
            </div>
        </div>
    </div>

    <!-- Main Container -->
    <div class="container">
        <!-- Sidebar -->
        <div class="sidebar">
            <h3>Navigation</h3>
            <ul class="sidebar-menu">
                <li><a class="active" onclick="loadDashboard()">📊 Dashboard</a></li>
                <li><a onclick="loadResources()">📦 Resources</a></li>
                <li><a onclick="loadAnalytics()">📈 Analytics</a></li>
                <li><a onclick="loadSettings()">⚙️ Settings</a></li>
                <li><a onclick="logout()">🚪 Logout</a></li>
            </ul>
        </div>

        <!-- Content Area -->
        <div class="content">
            <div id="mainContent">
                <div class="section">
                    <h2>Welcome to Resource Manager</h2>
                    <p>Select an option from the sidebar or navigation menu to get started.</p>
                </div>
            </div>
        </div>
    </div>

    <script>
        function toggleDropdown() {
            const dropdown = document.getElementById('dropdownMenu');
            dropdown.classList.toggle('show');
        }

        window.onclick = function(event) {
            const dropdown = document.getElementById('dropdownMenu');
            if (!event.target.closest('.user-profile')) {
                dropdown.classList.remove('show');
            }
        };

        function loadDashboard() {
            const content = `
                <div class="section">
                    <h2>Dashboard</h2>
                    <p>Welcome <%= username %>, this is your dashboard.</p>
                </div>
                <div class="section">
                    <h2>Quick Stats</h2>
                    <p>Total Resources: <strong id="totalResources">Loading...</strong></p>
                </div>
            `;
            document.getElementById('mainContent').innerHTML = content;
            loadResourceCount();
        }

        function loadResources() {
            const content = `
                <div class="section">
                    <h2>Resources</h2>
                    <div id="resourcesList">
                        <div class="loading">
                            <div class="spinner"></div>
                            <p>Loading resources...</p>
                        </div>
                    </div>
                </div>
            `;
            document.getElementById('mainContent').innerHTML = content;
            
            // Fetch resource details using jQuery AJAX
            $.ajax({
                url: '/api/v1/resourcemanager/resource/1',
                type: 'GET',
                dataType: 'json',
                success: function(data) {
                    let html = '<div class="resource-grid">';
                    html += '<div class="resource-card">';
                    html += '<h3>' + data.name + '</h3>';
                    html += '<p><strong>ID:</strong> ' + data.id + '</p>';
                    html += '<p><strong>Status:</strong> <span style="color: #27ae60;">Active</span></p>';
                    html += '</div>';
                    html += '</div>';
                    document.getElementById('resourcesList').innerHTML = html;
                },
                error: function(xhr, status, error) {
                    document.getElementById('resourcesList').innerHTML = 
                        '<div class="error-message">Error loading resources: ' + error + '</div>';
                }
            });
        }

        function loadResourceCount() {
            $.ajax({
                url: '/api/v1/resourcemanager/resource/1',
                type: 'GET',
                dataType: 'json',
                success: function(data) {
                    document.getElementById('totalResources').textContent = '1';
                },
                error: function() {
                    document.getElementById('totalResources').textContent = '0';
                }
            });
        }

        function loadAnalytics() {
            const content = `
                <div class="section">
                    <h2>Analytics</h2>
                    <p>Analytics data will be displayed here.</p>
                </div>
            `;
            document.getElementById('mainContent').innerHTML = content;
        }

        function loadSettings() {
            const content = `
                <div class="section">
                    <h2>Settings</h2>
                    <p>User Settings</p>
                    <div>
                        <p><strong>Username:</strong> <%= username %></p>
                        <p><strong>Session ID:</strong> <%= session.getId() %></p>
                    </div>
                </div>
            `;
            document.getElementById('mainContent').innerHTML = content;
        }

        function loadProfile() {
            const content = `
                <div class="section">
                    <h2>User Profile</h2>
                    <p><strong>Username:</strong> <%= username %></p>
                    <p><strong>Role:</strong> Administrator</p>
                </div>
            `;
            document.getElementById('mainContent').innerHTML = content;
        }

        function logout() {
            if (confirm('Are you sure you want to logout?')) {
                window.location.href = '/logout';
            }
        }

        // Load dashboard on page load
        document.addEventListener('DOMContentLoaded', function() {
            loadDashboard();
        });
    </script>
</body>
</html>
