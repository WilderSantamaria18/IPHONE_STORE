# PowerShell script to run Spring Boot application without Maven
Write-Host "Starting Spring Boot Application..." -ForegroundColor Green

# Check if Java is available
if (Get-Command java -ErrorAction SilentlyContinue) {
    Write-Host "Java found" -ForegroundColor Green
} else {
    Write-Host "Java not found in PATH" -ForegroundColor Red
    exit 1
}

# Set the main class
$mainClass = "com.idat.continua2.demo.Continua2Application"

# Navigate to project directory
Set-Location "d:\4 CICLO\DESARROLLO DE APLICACIONES 2\PRESENTACION\Desarrollo_Aplicacion_2\Continua2"

# Check if Maven is available
if (Get-Command mvn -ErrorAction SilentlyContinue) {
    Write-Host "Maven found - using mvn spring-boot:run" -ForegroundColor Green
    mvn spring-boot:run
} else {
    Write-Host "Maven not found. Please install Maven first." -ForegroundColor Red
    Write-Host "Alternative: Open this project in VS Code and use 'Spring Boot Dashboard'" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Quick Maven Install:" -ForegroundColor Cyan
    Write-Host "1. Download from: https://maven.apache.org/download.cgi" -ForegroundColor White
    Write-Host "2. Extract to: C:\tools\apache-maven-3.9.6" -ForegroundColor White
    Write-Host "3. Add to PATH: C:\tools\apache-maven-3.9.6\bin" -ForegroundColor White
    Write-Host "4. Restart PowerShell" -ForegroundColor White
    Write-Host "5. Run: mvn spring-boot:run" -ForegroundColor White
}
