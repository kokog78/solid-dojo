const https = require('https');
const fs = require('fs');
const Papa = require('papaparse');

const DOWNLOAD_URL = 'https://raw.githubusercontent.com/kokog78/solid-dojo/master/solid-dojo-1/data/licenses.csv';

class LicenseJob {

    constructor() {
        this.downloadUrl = DOWNLOAD_URL;
    }

    run(callback) {
        downloadFile(this.downloadUrl, stream => {
            parse(stream, licenses => {
                updateLicenses(licenses, () => {
                    callback(licenses);
                });
            });
        });
    }

};

function downloadFile(url, callback) {
    if (url.startsWith('file:///')) {
        let fileName = url.substring(8, url.length);
        let stream = fs.createReadStream(fileName);
        callback(stream);
    } else {
        https.get(url, response => {
            callback(response);
        });
    }
}

function parse(stream, callback) {
    Papa.parse(stream, {
        delimiter: ',',
        header: true,
        dynamicTyping: true,
        complete: results => {
            callback(results.data);
        },
        error: e => {
            console.error(e);
        }
    });
}

function updateLicenses(licenses, callback) {
    const fileName = __dirname + '../../../../data/licenses.json';
    fs.access(fileName, err => {
        let db;
        if (err) {
            db = [];
        } else {
            let content = fs.readFileSync(fileName).toString();
            db = JSON.parse(content);
        }
        for (let license of licenses) {
            db.push(license);
        }
        fs.writeFile(fileName, JSON.stringify(db), err => {
            if (err) {
                throw err;
            }
            callback();
        });
    });
}

module.exports = LicenseJob;