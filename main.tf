# main.tf

provider "aws" {
  region = "us-east-1"
}

resource "aws_instance" "ec2" {
  ami           = "ami-0133fb3dded749b65"
  instance_type = "t2.micro"
  key_name = data.aws_key_pair.KeyPair.key_name
}

data "aws_key_pair" "KeyPair" {
  key_name = "vockey"
  include_public_key = true
}

output "public_ip" {
  value = aws_instance.ec2.public_ip
}

# Criação da VPC
resource "aws_vpc" "main" {
  cidr_block       = "192.168.0.0/16"
  enable_dns_support = true
  enable_dns_hostnames = true

  tags = {
    Name = "AULA-TERRAFORM-VPC"
  }
}
