const balls = [];
document.addEventListener("DOMContentLoaded", function(event) { 
	let numBalls = 69;
	const height = document.body.scrollHeight;
	if (height > 1500) {
		numBalls = 100;
	}
	for (let i = 0; i < numBalls; i++) {
		let ball = document.createElement("div");
		ball.classList.add("ball");
		ball.style.background = randomColor({luminosity:'bright',format:'rgba',alpha:1});
		ball.style.left = `${Math.floor(Math.random() * 100)}vw`;
		ball.style.top = `${Math.floor(Math.random() * height)}px`;
		ball.style.transform = `scale(${Math.random()})`;
		ball.style.width = `${Math.random()}em`;
		ball.style.height = ball.style.width;
		balls.push(ball);
		document.body.append(ball);
	}
	// Keyframes
	balls.forEach((el, i, ra) => {
		let to = {
				x: Math.random() * (i % 2 === 0 ? -11 : 11),
				y: Math.random() * 12
		};
		
		let anim = el.animate(
				[
					{ transform: "translate(0, 0)" },
					{ transform: `translate(${to.x}rem, ${to.y}rem)` }
					],
					{
					duration: (Math.random() + 1) * 2000, // random duration
					direction: "alternate",
					fill: "both",
					iterations: Infinity,
					easing: "ease-in-out"
					}
		);
	});
});
