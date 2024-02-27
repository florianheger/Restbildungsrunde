import React, {useEffect} from "react";
import {useNavigate} from "react-router";

const Home = () => {
    const navigate = useNavigate()

    useEffect(() => {
        let isToken = false;
        const cookieString = document.cookie;
        const cookies = cookieString.split('; ');

        for (const cookie of cookies) {
            const [cookieName, cookieValue] = cookie.split('=');
            if (cookieName === 'Token') {
                isToken = true;
            }
        }

        if (!isToken) {
            navigate('/login');
        } else {
            navigate('/');
        }
    }, []);



    return (
        <div>
            <h1>Hello World!</h1>
        </div>
    );
};

export default Home;