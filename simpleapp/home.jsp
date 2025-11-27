<%@ page import="jakarta.servlet.http.*,jakarta.servlet.*" %>
<%@ page session="true" %>
<%
    String user = (String) session.getAttribute("username");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>SimpleApp - Dashboard</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <style>
    body {
      background: #f5f7fa;
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    }
    .navbar {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      box-shadow: 0 5px 15px rgba(0,0,0,0.1);
    }
    .navbar-brand {
      font-size: 22px;
      font-weight: 700;
      color: white !important;
    }
    .navbar-brand i {
      margin-right: 10px;
    }
    .sidebar {
      background: white;
      border-radius: 10px;
      box-shadow: 0 2px 10px rgba(0,0,0,0.08);
      height: fit-content;
      position: sticky;
      top: 20px;
    }
    .sidebar h5 {
      color: #333;
      font-weight: 700;
      border-bottom: 2px solid #667eea;
      padding-bottom: 15px;
      margin-bottom: 15px;
    }
    .sidebar ul {
      list-style: none;
      padding: 0;
    }
    .sidebar li {
      margin-bottom: 10px;
    }
    .sidebar a {
      color: #555;
      text-decoration: none;
      display: flex;
      align-items: center;
      padding: 10px 15px;
      border-radius: 8px;
      transition: all 0.3s ease;
    }
    .sidebar a:hover {
      background: #f0f0f0;
      color: #667eea;
      padding-left: 20px;
    }
    .sidebar i {
      margin-right: 10px;
      color: #667eea;
    }
    .main-content {
      background: white;
      border-radius: 10px;
      box-shadow: 0 2px 10px rgba(0,0,0,0.08);
      padding: 30px;
      animation: fadeIn 0.5s ease-out;
    }
    @keyframes fadeIn {
      from {
        opacity: 0;
      }
      to {
        opacity: 1;
      }
    }
    .welcome-header {
      border-bottom: 2px solid #667eea;
      padding-bottom: 20px;
      margin-bottom: 30px;
    }
    .welcome-header h2 {
      color: #333;
      font-weight: 700;
      margin-bottom: 5px;
    }
    .welcome-header p {
      color: #666;
      margin: 0;
    }
    .resource-card {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border-radius: 10px;
      padding: 30px;
      color: white;
      text-align: center;
      margin-top: 20px;
    }
    .resource-card i {
      font-size: 48px;
      margin-bottom: 15px;
    }
    .user-profile {
      display: flex;
      align-items: center;
      gap: 12px;
      cursor: pointer;
      transition: all 0.3s ease;
    }
    .user-avatar {
      width: 45px;
      height: 45px;
      border-radius: 50%;
      background: white;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: 700;
      color: #667eea;
    }
    .user-profile:hover {
      opacity: 0.8;
    }
  </style>
</head>
<body>
  <nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container-fluid">
      <a class="navbar-brand" href="#">
        <i class="bi bi-rocket"></i>SimpleApp
      </a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav ms-auto">
          <li class="nav-item">
            <div class="user-profile" onclick="logout()">
              <div class="user-avatar">
                <i class="bi bi-person"></i>
              </div>
              <span>Logout</span>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </nav>

  <div class="container-fluid" style="padding: 30px 20px;">
    <div class="row">
      <div class="col-md-3">
        <div class="sidebar">
          <h5><i class="bi bi-list"></i> Navigation</h5>
          <ul>
            <li><a href="#"><i class="bi bi-house"></i> Dashboard</a></li>
            <li><a href="#"><i class="bi bi-file-text"></i> Resources</a></li>
            <li><a href="#"><i class="bi bi-gear"></i> Settings</a></li>
            <li><a href="#"><i class="bi bi-question-circle"></i> Help</a></li>
          </ul>
        </div>
      </div>

      <div class="col-md-9">
        <div class="main-content">
          <div class="welcome-header">
            <h2>Welcome, <span style="color: #667eea;"><%= org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(user) %></span>! 👋</h2>
            <p>You're successfully logged in. Explore your dashboard below.</p>
          </div>

          <div class="resource-card">
            <i class="bi bi-collection"></i>
            <h5>Resources</h5>
            <p id="resource" style="margin-bottom: 0;">
              Loading resource details...
            </p>
          </div>

          <div class="row mt-5">
            <div class="col-md-4">
              <div class="card border-0 shadow-sm h-100">
                <div class="card-body text-center">
                  <i class="bi bi-activity" style="font-size: 32px; color: #667eea;"></i>
                  <h6 class="card-title mt-3">Activity</h6>
                  <p class="card-text text-muted">Track your activities</p>
                </div>
              </div>
            </div>
            <div class="col-md-4">
              <div class="card border-0 shadow-sm h-100">
                <div class="card-body text-center">
                  <i class="bi bi-graph-up" style="font-size: 32px; color: #764ba2;"></i>
                  <h6 class="card-title mt-3">Analytics</h6>
                  <p class="card-text text-muted">View your statistics</p>
                </div>
              </div>
            </div>
            <div class="col-md-4">
              <div class="card border-0 shadow-sm h-100">
                <div class="card-body text-center">
                  <i class="bi bi-shield-check" style="font-size: 32px; color: #667eea;"></i>
                  <h6 class="card-title mt-3">Security</h6>
                  <p class="card-text text-muted">Manage your security</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
  <script>
    $(document).ready(function() {
      fetchResourceDetails();
    });

    function fetchResourceDetails() {
      $.ajax({
        url: '<%= request.getContextPath() %>/api/v1/simpleapp/myresources/1',
        type: 'GET',
        dataType: 'json',
        timeout: 5000,
        success: function(data) {
          if (data.id && data.name) {
            $('#resource').html('ID: <strong>' + data.id + '</strong><br>Name: <strong>' + data.name + '</strong>');
            console.log('Successfully fetched resource: ', data);
          } else if (data.error) {
            $('#resource').html('Error: ' + data.error);
            console.error('Error from API: ', data.error);
          }
        },
        error: function(xhr, status, error) {
          if (xhr.status === 401 || xhr.status === 403) {
            console.warn('Session expired or unauthorized (HTTP ' + xhr.status + '). Redirecting to login...');
            setTimeout(function() {
              window.location.href = '<%= request.getContextPath() %>/';
            }, 1000);
          } else {
            console.error('AJAX error fetching resource:', status, error);
            $('#resource').html('Failed to load resource details. Status: ' + xhr.status);
          }
        }
      });
    }

    function logout() {
      if (confirm('Are you sure you want to logout?')) {
        location.href = '<%= request.getContextPath() %>/logout';
      }
    }
  </script>
</body>
</html>
