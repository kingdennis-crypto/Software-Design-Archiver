# File Archiver - Apollo 57

## Overview

The File Archiver project aims to provide efficient management of file archives on the user's machine.
Designed for individuals seeking to streamline the organization of multiple files into a single archive,
such as a ZIP file, this software offers a robust set of features tailored to meet diverse archival needs.

## Features

### Add Files/Folders to Archive

Enables users to effortlessly add multiple files and folders to a new archive,
facilitating seamless organization and consolidation.

### Extract Archive Contents

Facilitates the extraction of all contents from an archive into a user-defined folder,
ensuring easy access to archived files.

### Password-Based Encryption

Provides support for password-based encryption of created archives,
ensuring enhanced security for sensitive data. Additionally, supports decryption during the extraction process.

### Archive Content Exploration

Offers functionalities for exploring the contents of an archive without the need for extraction,
allowing users to preview and navigate archived files conveniently.

### Support for Multiple Compression Formats

Includes support for various compression formats, with ZIP being a default option.
Furthermore, the architecture allows for effortless addition of new compression formats, ensuring flexibility and adaptability.

### Basic Configuration Support

Supports basic configurations for each compression format, such as compression level,
ensuring users can tailor compression settings to their specific requirements.

### Generate Printable Report

This functionality allows users to visualize the content of the report using graphs.
Users can automatically generate a printable report summarizing archive contents.

## Usage

### Launching the Application

1. Ensure that you have JDK 11 or higher installed.
1. Open a terminal or command prompt.
1. Navigate to the directory containing the jar file.
1. Execute `java -jar <fat jar>` - replace `<fat jar>` with the name of the jar file.

### Adding a Folder to the archive

1. Choose the folder to include in the archive.
1. Press archive.

### Extracting Archive Contents

1. Select the archive to extract content from.
1. Provide the password if the archive is password protected.
1. Press dearchive.

### Password-Based Encryption

1. When creating a new archive, choose a password if password-locked is required.

### Support for Multiple Compression Formats

1. Open settings.
1. Choose the compression format to be desired.

## Prerequisites

Before running the File Archiver, ensure that you have the following prerequisite installed:

### Java Development Kit (JDK) 11

The File Archiver project requires JDK 11 or higher to be installed on your machine.
If you haven't installed it yet, you can download and install JDK 11 from the official Oracle website or any other trusted source.

## How to run

The project can be run by using the following command: `java -jar <fat jar>`

## Acknowledgements

- [Zip4j](https://github.com/srikanth-lingala/zip4j)
- [jarchivelib](https://github.com/thrau/jarchivelib?tab=readme-ov-file)

## Collaborators

- Dennis Moes - d.moes@student.vu.nl - 2839746
- Simon Vriesema - s.r.vriesema@student.vu.nl - 2839785
- Joli-Coeur Weibolt - j.r.f.weibolt@student.vu.nl - 2837627
- Jaïr Telting - j.s.student@vu.nl - 2691376
