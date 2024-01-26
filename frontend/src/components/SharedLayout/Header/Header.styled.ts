import styled from 'styled-components';

export const StylizedHeader = styled.header`
	height: auto;
	margin-bottom: 50px;
	padding: 9px 0;
	border-bottom: 1px solid ${props => props.theme.borderColor};
`;

export const HeaderElementsWrapper = styled.div`
	display: flex;
	justify-content: space-between;
	align-items: center;
`;

export const HeaderTitle = styled.h1`
	font-size: 24px;
	font-style: normal;
	font-weight: 400;
	line-height: normal;
	text-transform: uppercase;
`;
