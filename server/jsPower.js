var jsPowerStatic = {
	version: "1.0",
	funct: {}
}

function jsPowerEngine() {
    this.alert = function(message) {
        jsPower.alert(message);
    };

	this.getURLContent = function(url, callback, loading_prompt) {
		if(loading_prompt) {
			$.mobile.loading( 'show' );
		}
		var time = this.getCurrnetTime();
					
		jsPowerStatic.funct["getURLContent"+time] = function () {
			if(loading_prompt) {
				$.mobile.loading( 'hide' );
			}
			callback.apply(this, arguments);
		};
		jsPower.getURLContent(url, "jsPowerStatic.funct.getURLContent"+time);
	};
	
	this.parseHTMLtag = function(code, tag, callback, loading_prompt) {
		if(loading_prompt) {
			$.mobile.loading( 'show' );
		}
		var time = this.getCurrnetTime();

		jsPowerStatic.funct["parseHTMLtag"+time] = function () {
			if(loading_prompt) {
				$.mobile.loading( 'hide' );
			}
			callback.apply(this, arguments);
		};
		jsPower.parseHTMLtag(code, tag, "jsPowerStatic.funct.parseHTMLtag"+time);
	};

	this.getCurrnetTime = function() {
		return new Date().getTime();
	};
}