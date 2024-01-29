import React, { useEffect, useState } from "react";

function Profile() {
    const [user, setUser] = useState<any[]>([]);

    const fetchData = () => {
        return fetch("http://localhost:8080/api/user")
            .then((response) => response.json())
            .then((data) => setUser(data));
    }

    useEffect(() => {
        fetchData();
    },[])

    return (
        <main>
            <h1>User List</h1>
            <ul>
                {user && user.length > 0 && user.map((userObj, index) => (
                    <li key={userObj.id}> {userObj.username} with {userObj.points} Pts </li>
                ))}
            </ul>
        </main>
    );
}

export default Profile;