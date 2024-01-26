import { StylizedContainer } from './Container.styled';
import { ContainerProps } from './Container.types';

function Container({ children }: ContainerProps) {
	return <StylizedContainer>{children}</StylizedContainer>;
}

export default Container;
