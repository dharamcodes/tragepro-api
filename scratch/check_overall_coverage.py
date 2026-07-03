import xml.etree.ElementTree as ET

def get_overall_coverage(xml_path):
    try:
        tree = ET.parse(xml_path)
        root = tree.getroot()
    except Exception as e:
        print(f"Error parsing XML: {e}")
        return

    missed = 0
    covered = 0
    for counter in root.findall('./counter'):
        if counter.get('type') == 'LINE':
            missed = int(counter.get('missed'))
            covered = int(counter.get('covered'))
            break
            
    total = missed + covered
    if total > 0:
        ratio = covered / total
        print(f"Overall Line Coverage: {ratio * 100:.2f}% (Covered: {covered}, Total: {total})")
    else:
        print("No line counters found.")

if __name__ == '__main__':
    get_overall_coverage('build/reports/jacoco/test/jacocoTestReport.xml')
