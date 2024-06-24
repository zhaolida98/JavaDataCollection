import json
import csv


def process_json_to_csv(json_data):
    # Load the JSON data
    data = json.loads(json_data)

    # Open a file to write the CSV data
    with open('/home/nryet/JavaDataCollection/src/test/java/mvncentrorepo.csv', 'w', newline='') as file:
        writer = csv.writer(file)
        # Write the header row
        writer.writerow(['group', 'artifact', 'version'])

        for item in data:
            lib_name = item['lib_name']
            group, artifact = lib_name.split(":")
            # Sorting versions by date in ascending order
            sorted_versions = sorted(item['versions'], key=lambda x: x['date'])
            writer.writerow([group, artifact, ":".join([version['number'] for version in sorted_versions])])

print("CSV file has been created with the processed data.")
with open('/home/nryet/JavaDataCollection/src/test/java/reference_data.json', 'r') as file:
    json_data = file.read()
    process_json_to_csv(json_data)


