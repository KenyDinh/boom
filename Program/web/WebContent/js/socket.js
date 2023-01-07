var BoomSocket = function(url) {
	this.url = url;
	this.init = function(obj) {
		try {
			this.socket = new WebSocket(this.url);
		} catch (e) {
			console.log(JSON.stringify(e));
			return;
		}
		if (this.socket !== undefined && this.socket !== null) {
			if (obj !== undefined) {
				if (obj.onopen && typeof obj.onopen == 'function') {
					this.socket.onopen = obj.onopen;
				}
				if (obj.onmessage && typeof obj.onmessage == 'function') {
					this.socket.onmessage = obj.onmessage;
				}
				if (obj.onclose && typeof obj.onclose == 'function') {
					this.socket.onclose = obj.onclose;
				}
				if (obj.onerror && typeof obj.onerror == 'function') {
					this.socket.onerror = obj.onerror;
				}
			}
			window.onbeforeunload = () => {
				console.log("---closing---");
				this.close();
			}
		}
	}
	this.isOpen = function() {
		if (this.socket !== undefined && this.socket !== null && this.socket.readyState == WebSocket.OPEN) {
			return true;
		}
		return false;
	}
	this.close = function() {
		if (this.socket !== undefined && this.socket !== null) {
			this.socket.close();
			console.log("---socket closed---");
		}
	}
	this.sendMessage = function(msg) {
		if (this.isOpen()) {
			this.socket.send(msg);
		}
	}
}