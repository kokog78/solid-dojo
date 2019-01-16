const fs = require('fs');
const tmp = require('tmp');
const expect  = require('chai').expect;
const LicenseJob = require('../../main/javascript/license-job');

describe('LicenseJob', () => {

    const job = new LicenseJob();
    const fileName = __dirname + '/../../../data/licenses.json';
    let tempFile;

    beforeEach((done) => {
        fs.unlink(fileName, () => done());
    });

    afterEach(() => {
        if (tempFile) {
            fs.unlinkSync(tempFile.name);
        }
    });

    it('should download and process empty file', (done) => {
        initSource(['ID,TYPE,OWNER,VALID']);
        job.run(() => {
            expect(getResults()).deep.equal([]);
            done();
        });
    });

    it('should download file with one license', (done) => {
        initSource(['ID,TYPE,OWNER,VALID', 'A,B,C,2020-01-01']);
        job.run(() => {
            expect(getResults()).deep.equal([
                {
                    ID: 'A',
                    TYPE: 'B',
                    OWNER: 'C',
                    VALID: '2020-01-01'
                }
            ]);
            done();
        });

    });

    it('should download file with two licenses', (done) => {
        initSource(['ID,TYPE,OWNER,VALID', 'E,F,G,2021-02-02', 'H,I,J,2022-03-03']);
        job.run(() => {
            expect(getResults()).deep.equal([
                {
                    ID: 'E',
                    TYPE: 'F',
                    OWNER: 'G',
                    VALID: '2021-02-02'
                },
                {
                    ID: 'H',
                    TYPE: 'I',
                    OWNER: 'J',
                    VALID: '2022-03-03'
                }
            ]);
            done();
        });

    });

    function initSource(lines) {
        tempFile = tmp.fileSync({
            prefix: 'test-',
            postfix: '.csv'
        });
        fs.writeFileSync(tempFile.name, lines.join('\n'));
        job.downloadUrl = `file:///${tempFile.name}`;
    }

    function getResults() {
        let content = fs.readFileSync(fileName).toString();
        return JSON.parse(content);
    }

});