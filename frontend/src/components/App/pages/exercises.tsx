import React, {FormEvent, useEffect, useState} from "react";
import {useNavigate} from "react-router";

const Exercises = () => {
    const navigate = useNavigate();
    const [title, setTitleName] = useState("");
    const [description, setDescriptionName] = useState("");
    const [solution, setSolutionName] = useState("");
    const [language, setLanguageName] = useState("null_language");
    const [difficulty, setDifficultyName] = useState("null_difficulty");
    const [category, setCategoryName] = useState("");
    const [points, setPointsName] = useState(0);

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
            navigate('/exercises');
        }
    }, []);

    const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const response = await fetch('http://localhost:8080/api/exercise', {
            method: 'POST',
            headers: {
                'Access-Control-Allow-Origin': 'http://localhost:3000',
                'Content-Type': 'application/json',
            },
            mode: 'cors',
            credentials: 'include',
            body: JSON.stringify({ title, description, solution, language,
                difficulty, points, category }),
        })

    };

    return (
        <form onSubmit={(e) => handleSubmit(e)}>
            <div className="input-group">
                <label htmlFor="title">Titel</label>
                <input type="text" id="title" value={title} onChange={(e) => setTitleName(e.target.value)}/>
            </div>
            <div className="input-group">
                <label htmlFor="description">Aufgabe</label>
                <input type="text" id="description" value={description} onChange={(e) => setDescriptionName(e.target.value)}/>
            </div>
            <div className="input-group">
                <label htmlFor="solution">Lösung</label>
                <input type="text" id="solution" value={solution} onChange={(e) => setSolutionName(e.target.value)}/>
            </div>
            <div>
                <label htmlFor="difficulty">Difficulty:</label>
                <select name="difficulty" id="difficulty" value={difficulty} onChange={(e) => setDifficultyName(e.target.value)}>
                    <option value="null_difficulty">Auswählen</option>
                    <option value="easy">Leicht</option>
                    <option value="medium">Mittel</option>
                    <option value="hard">Schwer</option>
                </select>
            </div>
            <div>
                <label htmlFor="language">Sprache:</label>
                <select name="language" id="language" value={language}
                        onChange={(e) => setLanguageName(e.target.value)}>
                    <option value="null_language">Auswählen</option>
                    <option value="python">Python</option>
                    <option value="java">Java</option>
                    <option value="javascript">Javascript</option>
                    <option value="c">C</option>
                    <option value="c++">C++</option>
                    <option value="c#">C#</option>
                </select>
            </div>
            <div className="input-group">
                <label htmlFor="category">Kategorie</label>
                <input type="text" id="category" value={category} onChange={(e) => setCategoryName(e.target.value)}/>
            </div>
            <div>
                <label htmlFor="points">Punkte:   </label>
                <input type="number" id="points" name="points" min="1" max="10" value={points}
                       onChange={(e) => setPointsName(parseInt(e.target.value))}/>
            </div>
            <div>
                <button type="submit" className="submit-btn" id="submit">
                    einreichen
                </button>
            </div>
        </form>
    );
};

export default Exercises;