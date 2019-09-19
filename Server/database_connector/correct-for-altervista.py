import glob
import re
import os

def substitute(content, file):
    file = re.sub(r"\\", r"/", file)
    m = re.search(r'require_once "(.*?)";', content)
    if m:
        found = m.group(1)
        path = os.path.join(file, found)
        path = os.path.normpath(path)
        path = re.sub(r"\\", r"/", path)
        content = re.sub(r'''(require_once) "(.*?)";''', r'''\1 "/''' + path +  r'''";''', content)
    return content  

files = glob.glob("**/*.php", recursive=True)

for file in files:
    with open(file, "r") as f:
        content = f.read()

    content = substitute(content, 'membri\\fsc\\database_connector\\' + os.path.dirname(file))
    print(file)
    with open(file, "w") as f:
        f.write(content)