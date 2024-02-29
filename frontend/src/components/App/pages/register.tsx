import React, {useState} from 'react';
import { useNavigate } from 'react-router';

const RegisterForm = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleUsernameChange = (e: { target: { value: React.SetStateAction<string>; }; }) => {
        setUsername(e.target.value);
    };

    const handlePasswordChange = (e: { target: { value: React.SetStateAction<string>; }; }) => {
        setPassword(e.target.value);
    };

    const navigate = useNavigate()

    const handleSubmit = async (e: { preventDefault: () => void; }) => {
        e.preventDefault();

        const response = await fetch('http://localhost:8080/api/user/signup', {
            method: 'POST',
            headers: {
                'Access-Control-Allow-Origin': 'http://localhost:3000',
                'Content-Type': 'application/json',
            },
            mode: 'cors',
            credentials: 'include',
            body: JSON.stringify({ username, password }),
        })

        console.log(response);
        document.cookie = `Token=${(await response.json()).jwt}`;
        if (response.status == 201) {
            navigate("/exercises");
            console.log('Register successful!');
        } else {
            // Handle failed Register
            console.error('Register failed!');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label htmlFor="username">Username:</label>
                <input
                    type="text"
                    id="username"
                    value={username}
                    onChange={handleUsernameChange}
                />
            </div>
            <div>
                <label htmlFor="password">Password:</label>
                <input
                    type="password"
                    id="password"
                    value={password}
                    onChange={handlePasswordChange}
                />
            </div>
            <button type="submit" id="submit">Register</button>
        </form>
    );
};

export default RegisterForm;
