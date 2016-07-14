package flexygames

class TeamsController {

	def statsService
	
	def index = { redirect(action:"list") }

	def home = { redirect(action:"list") }

	def list = {
		// prepare default params values
		params.max = Math.min(params.max ? params.int('max') : 10, 30)
		if(!params.offset) params.offset = 0
		if(!params.sort) params.sort = "name"
		if(!params.order) params.order = "asc"
		def teams = Team.list(params)
		def teamsNbr = Team.count()
		[teamInstanceList: teams, teamInstanceTotal: teamsNbr]
	}

	def show = {
		def team = Team.get(params.id)
		if (!team) {
			flash.error = "${message(code: 'default.not.found.message', args: [message(code: 'team.label', default: 'Team'), params.id])}"
			return redirect(action: "list")
		}
		if (!params.mode) {
			/*if (team.getAllSubscribedSessionGroups(true).size() > 0) {
				params.mode = "competition"
			} else {
				params.mode = "training"
			}*/
			params.mode = "blog"
		}
		if (params.mode == "blog") {
			showBlog(params, team)
		}
		else if (params.mode == "ranking") {
			showRanking(params, team)
		} else {
			[teamInstance: team]
		}
	}
	def showRanking(params, team) {
		def criteria = (params.criteria ? params.criteria : 'statuses.doneGood')
		def sessionGroupId = (params.sessionGroupId ? Integer.parseInt(params.sessionGroupId) : 0)
		SessionGroup sessionGroup = (sessionGroupId > 0 ? SessionGroup.get(sessionGroupId) : null)
		def membersRanking = statsService.getTeamMembersRanking(team, criteria, sessionGroup)
		return [teamInstance: team, membersRanking: membersRanking, currentCriteria: criteria, currentSessionGroupId: sessionGroupId]
	}
	
	def showBlog(params, team) {
		params.teamId = team.id
		params.max = Math.min(params.max ? params.int('max') : 10, 30)
		if(!params.offset) params.offset = 0
		if(!params.sort) params.sort = "date"
		if(!params.order) params.order = "desc"
		// Get blog entries for all users of the team
		def userBlogEntries = team.getBlogEntries(params)
		// Create implicit blog entries from the sessions of the team
		def sessions = team.getSessions(params)
		def sessionBlogEntries = []
		sessions.each { session ->
			BlogEntry blogEntry = new BlogEntry()
			blogEntry.date = session.date
			blogEntry.team = session.group.defaultTeams[0]
			blogEntry.session = session
			if (session.name) {
				blogEntry.title = session.name + " (" + session.date + ")"
			} else {
				blogEntry.title = session.group.competition
			}
			sessionBlogEntries << blogEntry
		}
		// TODO merge the pagination between userBlogEntries and sessionBlogEntries
		def blogEntriesTotal = team.countSessions()
		def allBlogEntries = userBlogEntries + sessionBlogEntries
		allBlogEntries.sort()
		return [teamInstance: team, allBlogEntries: allBlogEntries, blogEntriesTotal: blogEntriesTotal, params: params]
	}

	def displayBlogEntry = {
		BlogEntry be = BlogEntry.get(params.id)
		if (!be) {
			flash.error = "${message(code: 'default.not.found.message', args: [message(code: 'blogEntry'), params.id])}"
			return redirect(action: "list")
		}
		render(view: 'blogEntry', model: ['blogEntry': be])
	}

}
