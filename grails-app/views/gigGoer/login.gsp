<html>
	<head>
		<title>Login Page</title>
		<meta name="layout" content="main" />
	</head>
	<body>
      <div class="body">
        <g:if test="${!session.user}">
            <g:if test="${flash.message}">
				<div class="message">
					${flash.message}
				</div>
			</g:if>

          <p>
          Welcome to your gigs list. Login below or create a new user: <span class="menuButton"><g:link class="create" action="create">New User</g:link></span>
          </p>
          <g:form action="handleLogin" method="post" >

              </br>
              <span class='nameClear'><label for="login">
						User Name:
                </label>
                </span>
              <g:select name='userName' from="${GigGoer.list()}"
				    optionKey="userName" optionValue="userName"></g:select>
              <br />
              <div class="buttons">
                  <span class="button"><g:actionSubmit value="Login" action="handleLogin" />
                    </span>
                </div>
            </g:form>

        </g:if>
        <g:else>
            <%-- ${response.sendRedirect(createLink(controller: 'gigGoer', action: 'show', id:'${session.user.id}'))} --%>
        </g:else>
      </div>
	</body>
</html>
