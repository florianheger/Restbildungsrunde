import { Outlet } from 'react-router-dom'
import Container from './Container/Container'
import Header from './Header/Header'

function SharedLayout() {
	return (
		<>
			<Header />
			<Container>
				<Outlet/>
			</Container>
		</>
	)
}

export default SharedLayout