RUNNING SEMTIX CLIENT
======================

- Install _openjdk-7-(jre|jdk)_ and _libreoffice_
- Setup and configure printer, so `/usr/bin/soffice -p` works
- Set `logo=0` in _sofficerc_, e.g. /etc/libreoffice/sofficerc, /usr/lib/libreoffice/program/sofficerc
- Copy **semtixconf.properties, log4j.properties and hibernate.cfg.xml to /etc/semtixdb/** and adjust them to your environment
- Create _/var/log/semtixdb/_ and make accessible for users that run semtix
- Copy templates folder to the place specified in semtixconf.properties
- Execute `mvn clean install` where the _pom.xml_ file is located whenever you want to build from source with jdk7

---

### FIRST STEPS ON CLIENT
- Datenbank -> Neues Semester Anlegen and create a new Semester
- Datenbank -> Aktuelles Semester and select the Semester as Global Semester
- AdminTools and initialize all relevant settings. That also entails clicking on "Speichern" at the path settings in case a field is missing from your semtixconf.properties
- Fill other data that is frequently used (e.g. Textbausteine)
- In DBHandlerTextbausteine.java you may add default text blocks for daily usage and/or development/testing
- Put your letter templates in the templates folder
- In ExternalSettings.java you can specifiy the String values for these templates
- May also need to set the templates in the semtixconf.properties 

--- 

### ON SERVER:
- create postgresql db (we use driver 9.1-901-1.jdbc4 from within hibernate 4.3.8)
  and make sure the db is accessible by clients (usually port 5432)
- install postgresql-contrib and execute _CREATE EXTENSION unaccent;_ on semtix db



