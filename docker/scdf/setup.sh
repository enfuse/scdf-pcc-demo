 #!/usr/bin/env sh
while ! nc -zw2 dataflow.localhost 9393 &>/dev/null; do
    true
done

apk add --no-cache curl
