export type UsersListProps = {
	arrData: arrDataType;
};

type arrDataType = Array<{
	username: string;
	points: number;
	id: number;
}>;
