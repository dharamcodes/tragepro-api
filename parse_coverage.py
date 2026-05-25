import xml.etree.ElementTree as ET

tree = ET.parse('build/reports/jacoco/test/jacocoTestReport.xml')
root = tree.getroot()

print("Class Coverage (Line Coverage < 95%):")
for package in root.findall('package'):
    for clazz in package.findall('class'):
        for counter in clazz.findall('counter'):
            if counter.get('type') == 'LINE':
                missed = int(counter.get('missed'))
                covered = int(counter.get('covered'))
                total = missed + covered
                if total > 0:
                    coverage = (covered / total) * 100
                    if coverage < 95:
                        print(f"{clazz.get('name')}: {coverage:.2f}% (Missed: {missed}, Covered: {covered})")
