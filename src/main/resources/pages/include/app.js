window.addEventListener('load', () => {
	modules = document.getElementsByClassName('module-info')

	hideModules()

	$('.module-list-item').on('click', function() {
		hideModules()
		showModule('info-' + this.id)
	})

	showModule('info-' + document.getElementsByClassName('module-list-item')[0].id)

	function hideModules() {
		for (i = 0; i < modules.length; i++) {
			modules[i].style.display = 'none'
		}	
	}

	function showModule(module) {
		for (i = 0; i < modules.length; i++) {
			console.log(module + ' - ' + modules[i].id)
			if (module === modules[i].id) {
			modules[i].style.display = 'block'
				break
			}
		}
	}
})