
<%@ page import="flexygames.Reminder" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="desktop">
		<g:set var="entityName" value="${message(code: 'reminder.label', default: 'Reminder')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-reminder" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-reminder" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<th><g:message code="reminder.session.label" default="Session" /></th>
					
						<g:sortableColumn property="minutesBeforeSession" title="${message(code: 'reminder.minutesBeforeSession.label', default: 'Minutes Before Session')}" />
					
						<g:sortableColumn property="jobExecuted" title="${message(code: 'reminder.jobExecuted.label', default: 'Job Executed')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${reminderInstanceList}" status="i" var="reminderInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${reminderInstance.id}">${fieldValue(bean: reminderInstance, field: "session")}</g:link></td>
					
						<td>${fieldValue(bean: reminderInstance, field: "minutesBeforeSession")}</td>
					
						<td><g:formatBoolean boolean="${reminderInstance.jobExecuted}" /></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${reminderInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
