# Assumptions

Ubuntu

```bash
lsb_release -a
No LSB modules are available.
Distributor ID: Ubuntu
Description:    Ubuntu 22.04.3 LTS
Release:        22.04
Codename:       jammy
```

Java

```bash
java --version
openjdk 19.0.2 2023-01-17
OpenJDK Runtime Environment (build 19.0.2+7-Ubuntu-0ubuntu322.04)
OpenJDK 64-Bit Server VM (build 19.0.2+7-Ubuntu-0ubuntu322.04, mixed mode, sharing)
```

# PostgreSQL

```bash
sudo apt-get update
sudo apt install postgresql postgresql-contrib libpq-dev
```

Adding the user for your app

```bash
sudo -i -u postgres
psql
```

```sql
CREATE ROLE <your_username> LOGIN SUPERUSER PASSWORD '<your_password>';
#
CREATE ROLE
```

PostgreSQL

```bash
dpkg -l | grep postgresql
ii  postgresql                                                  14+238                                      all          object-relational SQL database (supported version)
ii  postgresql-14                                               14.9-0ubuntu0.22.04.1                       amd64        The World's Most Advanced Open Source Relational Database
ii  postgresql-client-14                                        14.9-0ubuntu0.22.04.1                       amd64        front-end programs for PostgreSQL 14
ii  postgresql-client-common                                    238                                         all          manager for multiple PostgreSQL client versions
ii  postgresql-common                                           238                                         all          PostgreSQL database-cluster manager
ii  postgresql-contrib                                          14+238                                      all          additional facilities for PostgreSQL (supported version)
```