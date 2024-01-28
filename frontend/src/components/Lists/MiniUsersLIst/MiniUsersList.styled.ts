import styled from 'styled-components';

export const MiniUsersListDataWrapper = styled.ul`
	padding: 17px 15px;
	width: 380px;
	height: 100%;
	border-radius: 12px;
	background: #1d1d1d;
`;

export const MiniDataWrapper = styled.li`
	display: flex;
	justify-content: space-between;
	align-items: center;
	width: 100%;
	height: 42px;
	padding: 12px;
	border-radius: 12px;
	border: 1px solid #323232;
	background: black;

	&:not(:last-child) {
		margin-bottom: 18px;
	}
`;

export const MiniRightPartWrapper = styled.div`
	display: flex;
	justify-content: space-between;
	align-items: center;
	width: 75%;
`;

export const MiniNameAvatarWrapper = styled.div`
	display: flex;
	justify-content: space-between;
	align-items: center;
	gap: 10px;
`;

export const MiniAvatarWrapper = styled.div`
	width: 26px;
	height: 26px;
	border-radius: 50%;
	overflow: hidden;
`;

export const MiniUserAvatar = styled.img`
	width: 100%;
	height: auto;
`;

export const Place = styled.p`
	font-size: 20px;
`;

export const Name = styled.p`
	font-size: 16px;
	text-decoration-line: underline;
`;

export const Points = styled.p`
	font-size: 20px;
`;
