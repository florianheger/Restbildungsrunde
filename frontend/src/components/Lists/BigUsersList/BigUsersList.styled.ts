import styled from 'styled-components';

export const UserInfoWrapper = styled.ul`
	width: 100%;
`;

export const UserPlaceInfoWrapper = styled.li`
	display: flex;
	justify-content: space-between;
	align-items: center;
	width: 100%;
	height: 64px;
	padding: 12px;
	border-radius: 12px;
	border: 1px solid #323232;
	background: black;

	&:not(:last-child) {
		margin-bottom: 34px;
	}
`;

export const UserAvatarNameWrapper = styled.div`
	display: flex;
	justify-content: space-between;
	align-items: center;
	gap: 10px;
`;

export const UserAvatarWrapper = styled.div`
	width: 46px;
	height: 46px;
	border-radius: 50%;
	overflow: hidden;
`;

export const RightPartWrapper = styled.div`
	display: flex;
	justify-content: space-between;
	align-items: center;
	width: 75%;
`;

export const UserAvatar = styled.img`
	width: 100%;
	height: auto;
`;

export const Name = styled.p`
	text-decoration-line: underline;
	text-transform: capitalize;
`;

export const Points = styled.p`
	text-transform: capitalize;
`;
