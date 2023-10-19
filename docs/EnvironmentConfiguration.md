# Environment Configuration

`os_backend/src/main/resources/application-local.properties`

```bash
os.datasource.jdbcUrl=jdbc:postgresql://localhost/<SECRET>
os.datasource.username=<SECRET>
os.datasource.password=<SECRET>
os.datasource.hibernate.ddl-auto=none
os.flyway.enabled=false
server.port=5000
propublica.inajar.token.secret=<SECRET>
opensecrets.inajar.token.secret=<SECRET>
fec.inajar.token.secret=<SECRET>
# Turn up SQL logging
#spring.jpa.show-sql=true
#logging.level.org.hibernate.SQL=DEBUG
#logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
# This requires the "datasource-proxy-spring-boot-starter" library to work.
#logging.level.org.springframework=INFO
#logging.level.net.ttddyy.dsproxy.listener.logging.SLF4JQueryLoggingListener=debug
#decorator.datasource.datasource-proxy.count-query=true
# webclient logging
#logging.level.org.springframework.web.reactive.function.client.ExchangeFunctions=TRACE
# Web Servlet MVC condition
spring.mvc.pathmatch.matching-strategy=ant_path_matcher
spring.mvc.log-request-details=true
# Failed to resolve and search domain query for configured domains failed as well spring boot
# Happens without this setting
```

## nginx

```bash
server {
        listen 80;
        listen [::]:80;
        server_name <NAME GOOGLE CLOUD PROVIDES> fail_timeout=0;
        return 301 https://<NAME GOOGLE CLOUD PROVIDES>$request_uri;
}

server {
    listen 443 ssl;
    listen [::]:443 ssl;

    ssl on;
    # ssl_certificate /etc/nginx/ssl/XXXX.cert.pem;
    # ssl_certificate_key /etc/nginx/ssl/XXXX.key.pem;

    server_name <NAME GOOGLE CLOUD PROVIDES>;

    include snippets/self-signed.conf;
    include snippets/ssl-params.conf;

    location / {

      proxy_set_header        Host $host:$server_port;
      proxy_set_header        X-Real-IP $remote_addr;
      proxy_set_header        X-Forwarded-For $proxy_add_x_forwarded_for;
      proxy_set_header        X-Forwarded-Proto $scheme;
      proxy_pass http://127.0.0.1:5000;
      proxy_redirect http:// https://;
      add_header Pragma "no-cache";
    }
}
```

## Accessing staging

Note: _keep everything running in tmux remotely. When you do this you need to run the below command in the bare
terminal_

`gcloud compute ssh api-staging`

## Backup the Database

`pg_dump --table sectors os_db > `date +%F`.sectors.os_db.sql`