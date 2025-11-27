<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Simple App - Login</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    body {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    }
    .login-container {
      background: white;
      border-radius: 15px;
      box-shadow: 0 15px 50px rgba(0,0,0,0.3);
      padding: 50px;
      width: 100%;
      max-width: 420px;
      animation: slideIn 0.5s ease-out;
    }
    @keyframes slideIn {
      from {
        opacity: 0;
        transform: translateY(30px);
      }
      to {
        opacity: 1;
        transform: translateY(0);
      }
    }
    .login-header {
      text-align: center;
      margin-bottom: 35px;
    }
    .login-header h1 {
      color: #333;
      font-weight: 700;
      font-size: 32px;
      margin-bottom: 10px;
    }
    .login-header p {
      color: #666;
      font-size: 15px;
    }
    .form-group {
      margin-bottom: 20px;
    }
    .form-label {
      color: #333;
      font-weight: 600;
      margin-bottom: 10px;
    }
    .form-control {
      border: 2px solid #e0e0e0;
      border-radius: 10px;
      padding: 14px 16px;
      font-size: 15px;
      transition: all 0.3s ease;
    }
    .form-control:focus {
      border-color: #667eea;
      box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.15);
    }
    .btn-login {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border: none;
      border-radius: 10px;
      padding: 14px;
      font-size: 16px;
      font-weight: 600;
      width: 100%;
      margin-top: 10px;
      transition: all 0.3s ease;
      color: white;
    }
    .btn-login:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
      color: white;
    }
    .btn-login:active {
      transform: translateY(0);
    }
    .divider {
      margin: 30px 0;
      color: #ddd;
    }
    .footer-text {
      text-align: center;
      color: #999;
      font-size: 13px;
    }
  </style>
</head>
<body>
  <div class="login-container">
    <div class="login-header">
      <h1>🚀 SimpleApp</h1>
      <p>Welcome Back</p>
    </div>

    <% String error = request.getParameter("error");
      if (error != null) { %>
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
      <strong>⚠️ Login Failed!</strong>
      <% if ("empty".equals(error)) { %>
        Please enter a username.
      <% } else if ("invalid".equals(error)) { %>
        Invalid username format.
      <% } else { %>
        An error occurred. Please try again.
      <% } %>
      <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <% } %>

    <form action="login" method="POST">
      <div class="form-group">
        <label for="username" class="form-label">Username</label>
        <input type="text" class="form-control" id="username" name="username"
              placeholder="Enter your username" required maxlength="50">
      </div>
      <button type="submit" class="btn btn-login">Sign In</button>
    </form>

    <hr class="divider">
    <p class="footer-text">Demo: Enter any username to login</p>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
