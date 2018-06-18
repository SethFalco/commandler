const modules = document.getElementsByClassName('module-info')
const modulesList = document.getElementsByClassName('module-list-item')

window.addEventListener('load', () => {
	switchModule(modulesList[0])

	for(const m of modulesList) {
		m.addEventListener('click', () => { switchModule(m) })
	}
})

function switchModule(module) {
	hideModules()
	showModule('info-' + module.id)
}

function hideModules() {
	for (i = 0; i < modules.length; i++) {
		modules[i].style.display = 'none'
	}	
}

function showModule(module) {
	for (i = 0; i < modules.length; i++) {
		if (module !== modules[i].id) {
			continue 
		}

		console.log(module + ' - ' + modules[i].id)
		modules[i].style.display = 'block'
		break
	}
}