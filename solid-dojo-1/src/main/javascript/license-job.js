const download = require('download-file')
const fs = require('fs');
const tmp = require('tmp');
const Papa = require('papaparse');

const DOWNLOAD_URL = 'https://raw.githubusercontent.com/kokog78/solid-dojo/master/solid-dojo-1/data/licenses.csv';

function downloadFile(url, callback) {
    if (url.startsWith('file:///')) {
        callback(url.substring(8, url.length));
    } else {
        let file = tmp.fileSync({
            prefix: 'licenses-',
            postfix: '.csv'
        });
        download(url, {
            filename: file.name
        }, err => {
            if (err) {
                throw err;
            }
            callback(file);
        });
    }
}

function parse(fileName, callback) {
    let stream = fs.createReadStream(fileName);
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

module.exports = class LicenseJob {

    constructor() {
        this.downloadUrl = DOWNLOAD_URL;
    }

    run(callback) {
        downloadFile(this.downloadUrl, fileName => {
            parse(fileName, licenses => {
                updateLicenses(licenses, () => {
                    callback(licenses);
                });
            });
        });
    }

};