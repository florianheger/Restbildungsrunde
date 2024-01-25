import styled from 'styled-components'

export const UserPlaceInfoWrapper = styled.li`
	display: flex;
	justify-content: space-between;
	padding: 12px;
	border-radius: 12px;
	border: 1px solid #323232;
	background: transparent;
`;

export const UserAvatarNameWrapper = styled.div`
	display: flex;
	gap: 10px;
`;

export const UserAvatarWrapper = styled.div`
	width: 46px;
	height: 46px;
	border-radius: 50%;
	overflow: hidden;
`;

export const UserAvatar = styled.img`
width: 100%;
height: auto;
`